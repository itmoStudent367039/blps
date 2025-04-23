package ru.ifmo.common.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExistsByNameTariffResponse implements Serializable {

    private boolean exists;
}
