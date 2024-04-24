package com.shopmax.entity;

import com.shopmax.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // db에서 order by 예약어를 사용하므로 orders라고 지정
@Getter
@Setter
@ToString
public class Order { // 클래스명은 설계도이므로 복수형으로 쓰지 X
    
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime orderDate; // 주문일
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK
    private Member member; // Order가 Member를 참조한다.

    @OneToMany(mappedBy = "order", // 연관관계 주인을 설정(order는 주인이 아니에요)
            cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
}
