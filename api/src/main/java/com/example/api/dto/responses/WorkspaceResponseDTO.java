    package com.example.api.dto.responses;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.math.BigDecimal;
    import java.util.List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class WorkspaceResponseDTO {

        private Long workspaceId;
        private String type;
        private BigDecimal price;
        private List<ReservationResponseDTO> reservations;

    }
