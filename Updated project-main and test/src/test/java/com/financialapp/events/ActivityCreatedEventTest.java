package com.financialapp.events;

import com.financialapp.entity.FinancialActivity;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ActivityCreatedEventTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        Object source = new Object();
        FinancialActivity activity = new FinancialActivity(); // Just create the object
        
        // When
        ActivityCreatedEvent event = new ActivityCreatedEvent(source, activity);
        
        // Then - Only test what we can be sure about
        assertAll(
            () -> assertThat(event.getSource()).isSameAs(source),
            () -> assertThat(event.getActivity()).isSameAs(activity),
            () -> assertThat(event.getActivity()).isNotNull() // Just check it's not null
        );
    }

    @Test
    void testInheritanceFromApplicationEvent() {
        // Given
        Object source = new Object();
        FinancialActivity activity = new FinancialActivity();
        
        // When
        ActivityCreatedEvent event = new ActivityCreatedEvent(source, activity);
        
        // Then
        assertThat(event).isInstanceOf(ApplicationEvent.class);
    }

    @Test
    void testEventWithNullActivity() {
        // Given
        Object source = new Object();
        
        // When
        ActivityCreatedEvent event = new ActivityCreatedEvent(source, null);
        
        // Then
        assertAll(
            () -> assertThat(event.getSource()).isSameAs(source),
            () -> assertThat(event.getActivity()).isNull()
        );
    }

    @Test
    void testEventWithDifferentSourceTypes() {
        // Given
        String stringSource = "string-source";
        Integer integerSource = 42;
        FinancialActivity activitySource = new FinancialActivity();
        FinancialActivity activity = new FinancialActivity();
        
        // When
        ActivityCreatedEvent event1 = new ActivityCreatedEvent(stringSource, activity);
        ActivityCreatedEvent event2 = new ActivityCreatedEvent(integerSource, activity);
        ActivityCreatedEvent event3 = new ActivityCreatedEvent(activitySource, activity);
        
        // Then
        assertAll(
            () -> assertThat(event1.getSource()).isSameAs(stringSource),
            () -> assertThat(event2.getSource()).isSameAs(integerSource),
            () -> assertThat(event3.getSource()).isSameAs(activitySource),
            () -> assertThat(event1.getActivity()).isSameAs(activity),
            () -> assertThat(event2.getActivity()).isSameAs(activity),
            () -> assertThat(event3.getActivity()).isSameAs(activity)
        );
    }

    @Test
    void testActivityReferenceIntegrity() {
        // Given
        Object source = new Object();
        FinancialActivity originalActivity = new FinancialActivity();
        
        // When
        ActivityCreatedEvent event = new ActivityCreatedEvent(source, originalActivity);
        FinancialActivity retrievedActivity = event.getActivity();
        
        // Then - Verify it's the same instance
        assertThat(retrievedActivity).isSameAs(originalActivity);
    }

    @Test
    void testEventEqualityBasedOnReference() {
        // Given
        Object source1 = new Object();
        Object source2 = new Object();
        FinancialActivity activity1 = new FinancialActivity();
        FinancialActivity activity2 = new FinancialActivity();
        
        // When
        ActivityCreatedEvent event1 = new ActivityCreatedEvent(source1, activity1);
        ActivityCreatedEvent event2 = new ActivityCreatedEvent(source1, activity1);
        ActivityCreatedEvent event3 = new ActivityCreatedEvent(source2, activity2);
        
        // Then - Events should not be equal unless they are the same instance
        assertAll(
            () -> assertThat(event1).isNotEqualTo(event2),
            () -> assertThat(event1).isNotEqualTo(event3),
            () -> assertThat(event2).isNotEqualTo(event3)
        );
    }

    @Test
    void testToStringMethod() {
        // Given
        Object source = new Object();
        FinancialActivity activity = new FinancialActivity();
        
        // When
        ActivityCreatedEvent event = new ActivityCreatedEvent(source, activity);
        String toStringResult = event.toString();
        
        // Then - Should contain class name
        assertAll(
            () -> assertThat(toStringResult).contains("ActivityCreatedEvent"),
            () -> assertThat(toStringResult).isNotNull()
        );
    }
}