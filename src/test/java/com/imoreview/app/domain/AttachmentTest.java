package com.imoreview.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.imoreview.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attachment.class);
        Attachment attachment1 = new Attachment();
        attachment1.setId("id1");
        Attachment attachment2 = new Attachment();
        attachment2.setId(attachment1.getId());
        assertThat(attachment1).isEqualTo(attachment2);
        attachment2.setId("id2");
        assertThat(attachment1).isNotEqualTo(attachment2);
        attachment1.setId(null);
        assertThat(attachment1).isNotEqualTo(attachment2);
    }
}
