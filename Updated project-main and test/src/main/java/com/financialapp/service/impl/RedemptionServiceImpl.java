package com.financialapp.service.impl;

import com.financialapp.dto.RedemptionDTO;
import com.financialapp.entity.Redemption;
import com.financialapp.entity.RedemptionStatus;
import com.financialapp.entity.RewardCatalog;
import com.financialapp.entity.User;
import com.financialapp.repository.RedemptionRepository;
import com.financialapp.repository.RewardCatalogRepository;
import com.financialapp.repository.UserRepository;
import com.financialapp.service.RedemptionService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RedemptionServiceImpl implements RedemptionService {

    @Autowired RedemptionRepository redemptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RewardCatalogRepository catalogRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    private void applyRedemptionDates(Redemption redemption, RedemptionStatus status, LocalDateTime redeemedAtFromDto) {
        if (status == RedemptionStatus.SUCCESS) {
            LocalDateTime redeemedAt = redeemedAtFromDto != null ? redeemedAtFromDto : LocalDateTime.now();
            redemption.setRedeemedAt(redeemedAt);

            // Calculate expire date based on partner-defined validity duration (in days/weeks)
            Integer validityDays = redemption.getCatalogItem().getValidityDuration(); // e.g., store duration in catalog
            if (validityDays != null) {
                redemption.setExpiryDate(redeemedAt.plusDays(validityDays));
            } else {
                redemption.setExpiryDate(null);
            }
        } else {
            // Non-successful Redemption should have null dates
            redemption.setRedeemedAt(null);
            redemption.setExpiryDate(null);
        }
    }


    @Override
    @Transactional
    public RedemptionDTO createRedemption(RedemptionDTO dto) {
        User user = getUserRepository().findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        RewardCatalog catalogItem = getCatalogRepository().findByCatalogItemIdAndActiveTrue(dto.getCatalogItemId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Catalog item is inactive or does not exist: " + dto.getCatalogItemId()));

        RedemptionStatus status = dto.getStatus() != null ? dto.getStatus() : RedemptionStatus.PENDING;

        // Points check + deduct only if creating directly with SUCCESS status
        if (status == RedemptionStatus.SUCCESS) {
            if (user.getPoints() < catalogItem.getPointsRequired()) {
                throw new IllegalStateException("Insufficient points to redeem this item.");
            }
            user.setPoints(user.getPoints() - catalogItem.getPointsRequired());
            getUserRepository().save(user);
        }

        Redemption redemption = new Redemption();
        redemption.setUser(user);
        redemption.setCatalogItem(catalogItem);
        redemption.setStatus(status);
        redemption.setFulfillmentDetails(dto.getFulfillmentDetails());
        redemption.setFailureReason(dto.getFailureReason());
        redemption.setRedemptionCode(dto.getRedemptionCode() != null ? dto.getRedemptionCode() : generateRedemptionCode());

     // Apply dates according to status
        applyRedemptionDates(redemption, status, dto.getRedeemedAt());

        Redemption saved = redemptionRepository.save(redemption);

        RedemptionDTO out = getModelMapper().map(saved, RedemptionDTO.class);
        out.setUserId(saved.getUser().getUserId());
        if (saved.getCatalogItem() != null) out.setCatalogItemId(saved.getCatalogItem().getCatalogItemId());
        return out;
    }

    
    @Override
    public List<RedemptionDTO> getAllRedemptions() {
        return redemptionRepository.findAll()
                .stream()
                .map(r -> {
                    RedemptionDTO dto = getModelMapper().map(r, RedemptionDTO.class);
                    dto.setUserId(r.getUser().getUserId());
                    if (r.getCatalogItem() != null) {
                        dto.setCatalogItemId(r.getCatalogItem().getCatalogItemId());
                    }
                    return dto;
                })
                .toList();
    }
    
    @Override
    public RedemptionDTO getRedemptionById(Integer id) {
        Redemption saved = redemptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Redemption not found"));
        RedemptionDTO out = getModelMapper().map(saved, RedemptionDTO.class);
        out.setUserId(saved.getUser().getUserId());
        if (saved.getCatalogItem() != null) {
            out.setCatalogItemId(saved.getCatalogItem().getCatalogItemId());
        }
        return out;
    }


    @Override
    @Transactional
    public RedemptionDTO updateRedemption(Integer id, RedemptionDTO dto) {
        Redemption existing = redemptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Redemption not found"));

        User user = existing.getUser();
        RewardCatalog oldCatalog = existing.getCatalogItem();
        int oldCost = oldCatalog != null ? oldCatalog.getPointsRequired() : 0;

        // Determine new catalog
        RewardCatalog newCatalog = oldCatalog;
        if (dto.getCatalogItemId() != null) {
            newCatalog = getCatalogRepository().findByCatalogItemIdAndActiveTrue(dto.getCatalogItemId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Catalog item is inactive or does not exist: " + dto.getCatalogItemId()));
        }
        int newCost = newCatalog != null ? newCatalog.getPointsRequired() : 0;

        RedemptionStatus oldStatus = existing.getStatus();
        RedemptionStatus newStatus = dto.getStatus() != null ? dto.getStatus() : oldStatus;

        boolean oldWasRefunded = oldStatus == RedemptionStatus.FAILED || oldStatus == RedemptionStatus.REFUNDED;
        boolean newIsRefunded = newStatus == RedemptionStatus.FAILED || newStatus == RedemptionStatus.REFUNDED;

        // -------- Catalog change logic --------
        if (newCatalog != oldCatalog) {
            if (oldCost > 0 && oldStatus == RedemptionStatus.SUCCESS) {
                user.setPoints(user.getPoints() + oldCost);
            }
            if (newCost > 0 && newStatus == RedemptionStatus.SUCCESS) {
                if (user.getPoints() < newCost) {
                    throw new IllegalStateException("Insufficient points to switch to new catalog item.");
                }
                user.setPoints(user.getPoints() - newCost);
            }
            existing.setCatalogItem(newCatalog);
        }

        // -------- Status change logic --------
        if (oldStatus == RedemptionStatus.PENDING && newStatus == RedemptionStatus.SUCCESS) {
            if (user.getPoints() < newCost) {
                throw new IllegalStateException("Insufficient points to complete redemption.");
            }
            user.setPoints(user.getPoints() - newCost);
        }

        if (oldStatus == RedemptionStatus.SUCCESS && newIsRefunded) {
            user.setPoints(user.getPoints() + newCost);
        }

        if (oldWasRefunded && newStatus == RedemptionStatus.SUCCESS) {
            if (user.getPoints() < newCost) {
                throw new IllegalStateException("Insufficient points to re-apply redemption.");
            }
            user.setPoints(user.getPoints() - newCost);
        }

        getUserRepository().save(user);

        // -------- Dates handling --------
        if (newStatus == RedemptionStatus.SUCCESS) {
            LocalDateTime now = LocalDateTime.now();
            existing.setRedeemedAt(existing.getRedeemedAt() != null ? existing.getRedeemedAt() : now);

            if (newCatalog.getValidityDuration() != null) {
                existing.setExpiryDate(existing.getRedeemedAt().plusDays(newCatalog.getValidityDuration()));
            }
        } else {
            existing.setRedeemedAt(null);
            existing.setExpiryDate(null);
        }

        // -------- Update redemption details --------
        existing.setStatus(newStatus);
        existing.setFulfillmentDetails(dto.getFulfillmentDetails());
        existing.setFailureReason(dto.getFailureReason());

        if (dto.getRedemptionCode() != null && !dto.getRedemptionCode().isBlank()) {
            existing.setRedemptionCode(dto.getRedemptionCode());
        } else if (existing.getRedemptionCode() == null) {
            existing.setRedemptionCode(generateRedemptionCode());
        }

        if (dto.getUserId() != null && !dto.getUserId().equals(user.getUserId())) {
            User newUser = getUserRepository().findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            existing.setUser(newUser);
        }

        Redemption saved = redemptionRepository.save(existing);

        RedemptionDTO out = getModelMapper().map(saved, RedemptionDTO.class);
        out.setUserId(saved.getUser().getUserId());
        if (saved.getCatalogItem() != null) {
            out.setCatalogItemId(saved.getCatalogItem().getCatalogItemId());
        }

        return out;
    }


    @Override
    public void deleteRedemption(Integer id) {
        redemptionRepository.deleteById(id);
    }

    // small branded code generator
    private String generateRedemptionCode() {
        return "BB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


	public UserRepository getUserRepository() {
		return userRepository;
	}


	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	public RewardCatalogRepository getCatalogRepository() {
		return catalogRepository;
	}


	public void setCatalogRepository(RewardCatalogRepository catalogRepository) {
		this.catalogRepository = catalogRepository;
	}


	public ModelMapper getModelMapper() {
		return modelMapper;
	}


	public void setModelMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	
}
