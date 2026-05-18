package by.alexeysavchic.beer_pet_project.entity;

import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.entity.enums.WaveStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Wave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private WaveStatus status;

    @Column(name = "type_of_subscription")
    private TypeOfSubscription typeOfSubscription;

    @Column(name = "processed_date")
    private LocalDateTime processedDate;

    @Column(name = "year")
    private Year year;

    @Column(name = "month")
    private Month month;

    @OneToOne(mappedBy = "wave", cascade = CascadeType.ALL)
    private OrderSet orderSet;

    @OneToMany(mappedBy = "wave", cascade = CascadeType.ALL)
    private List<Order> orders;

    public void addOrder(Order order) {
        if (order.getWave() == null) {
            order.setWave(this);
        }
        orders.add(order);
    }

    public void setOrderSet(OrderSet orderSet) {
        this.orderSet = orderSet;
        orderSet.setWave(this);
    }
}
