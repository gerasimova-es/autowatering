/*
 * This file is generated by jOOQ.
 */
package com.home.autowatering.entiry.jooq.tables.pojos;


import javax.annotation.Generated;
import java.io.Serializable;
import java.sql.Date;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "http://www.jooq.org",
                "jOOQ version:3.11.5"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
@Deprecated
public class TankState implements Serializable {

    private static final long serialVersionUID = -567098564;

    private final Long id;
    private final Date date;
    private final Double filled;
    private final String name;
    private final Double volume;

    public TankState(TankState value) {
        this.id = value.id;
        this.date = value.date;
        this.filled = value.filled;
        this.name = value.name;
        this.volume = value.volume;
    }

    public TankState(
            Long id,
            Date date,
            Double filled,
            String name,
            Double volume
    ) {
        this.id = id;
        this.date = date;
        this.filled = filled;
        this.name = name;
        this.volume = volume;
    }

    public Long getId() {
        return this.id;
    }

    public Date getDate() {
        return this.date;
    }

    public Double getFilled() {
        return this.filled;
    }

    public String getName() {
        return this.name;
    }

    public Double getVolume() {
        return this.volume;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TankState (");

        sb.append(id);
        sb.append(", ").append(date);
        sb.append(", ").append(filled);
        sb.append(", ").append(name);
        sb.append(", ").append(volume);

        sb.append(")");
        return sb.toString();
    }
}