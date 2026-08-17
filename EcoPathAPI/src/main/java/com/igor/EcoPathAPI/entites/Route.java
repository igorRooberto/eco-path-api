package com.igor.EcoPathAPI.entites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "tb_route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",nullable = false,unique = true,updatable = false)
    private UUID id;

    @Column(name = "origin_name",nullable = false)
    private String originName;

    @Column(name = "destination_name",nullable = false)
    private String destinationName;

    @Column(name = "estimated_time",nullable = false)
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration estimatedTime;

    @Column(name = "distance_in_centimeters",nullable = false)
    private int distanceInCentimeters;

}
