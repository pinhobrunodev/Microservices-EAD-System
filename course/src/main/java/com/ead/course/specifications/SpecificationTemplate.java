package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {
    // OR = One or Other
    // AND = Return All
    @And({
            // Attribute of Entity that will be filtered (Enum = Equal)
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "courseLevel", spec = Equal.class),
            // Ex > Java .... return all Java courses on Database
            @Spec(path = "name", spec = Like.class)
    })

    public interface CourseSpec extends Specification<CourseModel> {
    }


}
