package by.alexeysavchic.beer_pet_project.dto.request;

import by.alexeysavchic.beer_pet_project.entity.enums.SplitType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSetSplitRequest {

    @NotEmpty
    private List<Long> ids;

    private SplitType splitType;
}
