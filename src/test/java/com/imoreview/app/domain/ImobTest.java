package com.imoreview.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.imoreview.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Imob.class);
        Imob imob1 = new Imob();
        imob1.setId("id1");
        Imob imob2 = new Imob();
        imob2.setId(imob1.getId());
        assertThat(imob1).isEqualTo(imob2);
        imob2.setId("id2");
        assertThat(imob1).isNotEqualTo(imob2);
        imob1.setId(null);
        assertThat(imob1).isNotEqualTo(imob2);
    }
}
