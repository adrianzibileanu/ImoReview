package com.imoreview.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.imoreview.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sub.class);
        Sub sub1 = new Sub();
        sub1.setId("id1");
        Sub sub2 = new Sub();
        sub2.setId(sub1.getId());
        assertThat(sub1).isEqualTo(sub2);
        sub2.setId("id2");
        assertThat(sub1).isNotEqualTo(sub2);
        sub1.setId(null);
        assertThat(sub1).isNotEqualTo(sub2);
    }
}
