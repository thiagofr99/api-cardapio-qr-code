package com.devthiagofurtado.cardapioqrcode.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioVO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String fullName;

    @NotEmpty
    private String password;

    private Boolean enabled;

    @NotEmpty
    private List<PermissionVO> permissions;

    private LocalDate dateLicense;

}
