package com.ead.authuser.specifications;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import java.util.UUID;

public class SpecificationTemplate {
    // OR = One or Other
    // AND = Return All
    @And({
            // Attribute of Entity that will be filtered (Enum = Equal)
            @Spec(path = "userType", spec = Equal.class),
            @Spec(path = "userStatus", spec = Equal.class),
            // Ex > Michelle .... return all Michele email's on Database
            @Spec(path = "email", spec = Like.class),

            @Spec(path = "fullName", spec = LikeIgnoreCase.class)
    })

    public interface UserSpec extends Specification<UserModel> {
    }




    public static Specification<UserModel> userCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<UserModel, UserCourseModel>userProd = root.join("usersCourses"); // Join between the 2 entities
            return cb.equal(userProd.get("courseId"),courseId);
            // select  distinct * from tb_users join usersCourses where courseId = :courseId
        };
    }


}
