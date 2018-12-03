package com.home.autowatering.model.filter

import com.home.autowatering.model.Pot
import java.util.*

class PotStateFilter(builder: Builder) {
    var pot: Pot? = null
    var from: Date? = null
    var to: Date? = null

    init {
        this.pot = builder.pot
        this.from = builder.from
        this.to = builder.to
    }

    companion object Builder {
        private var pot: Pot? = null
        private var from: Date? = null
        private var to: Date? = null

        fun withPot(pot: Pot): Builder {
            this.pot = pot
            return this
        }

        fun from(from: Date?): Builder {
            this.from = from
            return this
        }

        fun to(to: Date?): Builder {
            this.to = to
            return this
        }

        fun build(): PotStateFilter {
            return PotStateFilter(this)
        }
    }
}