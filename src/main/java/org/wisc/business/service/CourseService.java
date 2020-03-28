package org.wisc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wisc.business.dao.CourseDAO;
import org.wisc.business.dao.TermDAO;
import org.wisc.business.model.BusinessModel.Course;
import org.wisc.business.model.BusinessModel.Term;
import org.wisc.business.model.PVModels.CoursePV;
import org.wisc.business.model.PVModels.TermPV;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    CourseDAO courseDAO;

    @Resource
    TermService termService;

    public CoursePV convertCourseToCoursePV(Course course) {
        List<Term> terms = new LinkedList<>();
        if (course.getTermsIds() != null) {
            course.getTermsIds().forEach((tId)->terms.add(termService.findRawById(tId)));
        }
        return new CoursePV(course, terms);
    }

    public List<CoursePV> convertCoursesToCoursePVs(List<Course> courses) {
        List<CoursePV> results = new LinkedList<>();
        courses.forEach((c)->results.add(convertCourseToCoursePV(c)));
        return results;
    }

    public Course findRawById(String id) {
        if (id == null)
            return null;
        Optional<Course> result =  courseDAO.findById(id);
        if (!result.isPresent())
            return null;
        return result.get();
    }

    public CoursePV findById(String id) {
        return convertCourseToCoursePV(findRawById(id));
    }

    public CoursePV findByName(String name) {
        return convertCourseToCoursePV(courseDAO.findByName(name));
    }

    public List<CoursePV> all() {
        return convertCoursesToCoursePVs(courseDAO.findAll());
    }

    public CoursePV add(Course course) {
        if (course.getTermsIds() == null)
            course.setTermsIds(new LinkedList<>());
        return convertCourseToCoursePV(courseDAO.save(course));
    }

    /**
     * Update raw(safe upload) for back-reference update to avoid stack-overflow
     * Never update status for other raw objects!
     * @param course
     * @return
     */
    public Course updateRaw(Course course) {
        return courseDAO.save(course);
    }

    public CoursePV update(Course course) {
        Course oldCourse = findRawById(course.getId());
        if (oldCourse == null)
            return null;
        if (course.getName() != null && !course.getName().equals(oldCourse.getName()))
            oldCourse.setName(course.getName());
        if (course.getDescription() != null && !course.getDescription().equals(oldCourse.getDescription()))
            oldCourse.setDescription(course.getDescription());
        if (course.getTermsIds() != null) {
            HashSet<String> originalSet =
                    new HashSet<>(oldCourse.getTermsIds());
            LinkedList<String> newTids = new LinkedList<>();
            course.getTermsIds().forEach((tid)->{
                if (originalSet.contains(tid)) {
                    // keep
                    originalSet.remove(tid);
                    newTids.add(tid);
                } else {
                    // to update
                    Term t = termService.findRawById(tid);
                    if (t != null) {
                        t.setCourseId(course.getId());
                        termService.updateRaw(t);
                    }
                    newTids.add(tid);
                }
            });
            originalSet.forEach((tid)->{
                // unlink
                Term t = termService.findRawById(tid);
                if (t != null) {
                    t.setCourseId(null);
                    termService.updateRaw(t);
                }
            });
            oldCourse.setTermsIds(newTids);
        }
        return convertCourseToCoursePV(courseDAO.save(oldCourse));
    }

    public boolean delete(Course course) {
        if (findById(course.getId()) == null)
            return false;
        List<TermPV> termPVS = termService.findByCourseId(course.getId());
        termPVS.forEach((tPV)->{
            termService.delete(tPV.toRawType());
        });
        courseDAO.delete(course);
        return true;
    }
}
