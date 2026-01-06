package by.alexeysavchic.beer_pet_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_subscription")
public class UserSubscription
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "subscribe_date")
    @PastOrPresent
    private LocalDateTime subscribeDate;

    @Column(name = "time_of_expiration")
    @FutureOrPresent
    private LocalDateTime timeOfExpiration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
    name = "beer_user_subscription",
    joinColumns = {@JoinColumn(name = "beer_id")},
    inverseJoinColumns = {@JoinColumn(name = "user_subscription_id")})
    private Set<Beer> beers;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
}
