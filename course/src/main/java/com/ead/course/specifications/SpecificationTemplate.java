package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {
    // OR = One or Other
    // AND = Return All
    @And({
            // Attribute of Entity that will be filtered (Enum = Equal)
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "courseLevel", spec = Equal.class),
            // Ex > Java .... return all Java courses on Database
            @Spec(path = "name", spec = LikeIgnoreCase.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {
    }

    @And({ @Spec(path = "title", spec = LikeIgnoreCase.class) })
    public interface ModuleSpec extends Specification<ModuleModel> {
    }

    @And({ @Spec(path = "title", spec = LikeIgnoreCase.class) })
    public interface LessonSpec extends Specification<LessonModel> {
    }


    /*

    1- query.distinct(true) = Eliminated Duplicate Data
    2- Root = Entities that will be on the Query
    3- Expression<Collection> = Extract the collection of ModuleModel (A) that is present on the CourseModel (B)
    4-
       . Criteria Builder =  cb
       . and = Because we need the combination...
       . Get into course the courseId that come on the Parameter
       . And made a Subselect of what modules are present on the collection of the specific course that was found by the courseId

     */
    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<ModuleModel> module = root;
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<ModuleModel>> courseModules = course.get("modules");
            return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(module, courseModules));
        };
    }


    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<LessonModel> lesson = root;
            Root<ModuleModel> module = query.from(ModuleModel.class);
            Expression<Collection<LessonModel>> lessonModules = module.get("lessons");  // Module reference lessons collections to LessonModel
            return cb.and(cb.equal(module.get("moduleId"), moduleId), cb.isMember(lesson, lessonModules));
        };
    }


    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<CourseModel, CourseUserModel> courseProd = root.join("coursesUsers"); // Join between the 2 entities
            return cb.equal(courseProd.get("userId"),userId);
        };
    }


}
