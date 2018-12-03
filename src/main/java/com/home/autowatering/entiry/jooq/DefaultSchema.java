/*
 * This file is generated by jOOQ.
 */
package com.home.autowatering.entiry.jooq;


import com.home.autowatering.entiry.jooq.tables.HibernateSequence;
import com.home.autowatering.entiry.jooq.tables.Pot;
import com.home.autowatering.entiry.jooq.tables.PotState;
import com.home.autowatering.entiry.jooq.tables.TankState;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
public class DefaultSchema extends SchemaImpl {

    /**
     * The reference instance of <code></code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();
    private static final long serialVersionUID = -1667706467;
    /**
     * The table <code>hibernate_sequence</code>.
     */
    public final HibernateSequence HIBERNATE_SEQUENCE = HibernateSequence.HIBERNATE_SEQUENCE;

    /**
     * The table <code>pot</code>.
     */
    public final Pot POT = Pot.POT;

    /**
     * The table <code>pot_state</code>.
     */
    public final PotState POT_STATE = PotState.POT_STATE;

    /**
     * The table <code>tank_state</code>.
     */
    public final TankState TANK_STATE = TankState.TANK_STATE;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
                HibernateSequence.HIBERNATE_SEQUENCE,
                Pot.POT,
                PotState.POT_STATE,
                TankState.TANK_STATE);
    }
}