package by.constructioncompany.entity.facility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facility {

    private int id;
    private double square;
    private FacilityType facilityType;
}
