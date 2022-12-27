package com.imoreview.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.imoreview.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttachmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachmentDTO.class);
        AttachmentDTO attachmentDTO1 = new AttachmentDTO();
        attachmentDTO1.setId("id1");
        AttachmentDTO attachmentDTO2 = new AttachmentDTO();
        assertThat(attachmentDTO1).isNotEqualTo(attachmentDTO2);
        attachmentDTO2.setId(attachmentDTO1.getId());
        assertThat(attachmentDTO1).isEqualTo(attachmentDTO2);
        attachmentDTO2.setId("id2");
        assertThat(attachmentDTO1).isNotEqualTo(attachmentDTO2);
        attachmentDTO1.setId(null);
        assertThat(attachmentDTO1).isNotEqualTo(attachmentDTO2);
    }
}
