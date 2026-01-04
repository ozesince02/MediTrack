package com.airtribe.meditrack.interfaces;

import java.math.BigDecimal;

/**
 * Something that can be paid for.
 */
public interface Payable {
    BigDecimal amountDue();
}


