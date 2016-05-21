package org.ninjav.pda;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by ninjav on 2016/05/08.
 */
public class ConsolidationEngineFunctonialTest {
    @Test
    public void canCreateConsolidationEngine() {
        ConsolidationEngine e = new ConsolidationEngine();
        assertThat(e, is(notNullValue()));
    }

    @Test
    public void givenAccountsWithCreditorPayments_consolidateToCreditor() {
        
    }
}