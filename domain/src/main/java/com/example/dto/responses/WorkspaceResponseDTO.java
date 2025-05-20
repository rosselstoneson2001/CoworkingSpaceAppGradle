    package com.example.dto.responses;

    import lombok.Data;

    import java.math.BigDecimal;
    import java.util.List;

    @Data
    public class WorkspaceResponseDTO {

        private Long workspaceId;
        private String type;
        private BigDecimal price;
        private List<ReservationResponseDTO> reservations;

    }
