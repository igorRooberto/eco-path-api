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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",nullable = false,unique = true,updatable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "origin_name",nullable = false)
    private String originName;

    @Column(name = "destination_name",nullable = false)
    private String destinationName;

    @Column(name = "duration_in_seconds", nullable = false)
    private int durationInSeconds;

    @Column(name = "distance_in_Meters",nullable = false)
    private int distanceInMeters;

}
