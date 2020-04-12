package org.wisc.business.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wisc.business.model.BusinessModel.Professor;
import org.wisc.business.model.PVModels.ProfessorPV;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProfessorServiceTest {
    @Autowired
    ProfessorService professorService;

    @Test
    void findRawById() {
        final String validId = "5e7f2156783c1655d6255a94";
        final String invalidId = "5e7f2156783c1655d6255321";
        assertNotNull(professorService.findRawById(validId));
        assertNull(professorService.findRawById(invalidId));
        assertNull(professorService.findRawById(null));
    }

    @Test
    void findById() {
        final String validId = "5e7f2156783c1655d6255a94";
        final String invalidId = "5e7f2156783c1655d6255321";
        assertNotNull(professorService.findById(validId));
        assertNull(professorService.findById(invalidId));
        assertNull(professorService.findById(null));
    }

    @Test
    void findByName() {
        final String validName = "For Test";
        final String invalidName = "Tset Rof";
        assertFalse(professorService.findByName(validName).isEmpty());
        assertTrue(professorService.findByName(invalidName).isEmpty());
        assertTrue(professorService.findByName(null).isEmpty());
    }

    @Test
    void findFirstOccuranceByName() {
        final String validName = "For Test";
        final String invalidName = "Tset Rof";
        assertNotNull(professorService.findFirstOccuranceByName(validName));
        assertNull(professorService.findFirstOccuranceByName(invalidName));
        assertNull(professorService.findFirstOccuranceByName(null));
    }

    @Test
    void all() {
        List<ProfessorPV> all = professorService.all();
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    void add() {
        final Professor validProfessor =
                Professor.builder().name("New Prof").build();
        final Professor invalidProfessor = new Professor();
        final ProfessorPV newProfessor = professorService.add(validProfessor);
        assertNotNull(newProfessor);
        // cleanup the db
        assertTrue(professorService.delete(newProfessor.toRawType()));

        assertNull(professorService.add(invalidProfessor));
        assertNull(professorService.add(null));
    }

    @Test
    void updateRaw() {
        final String validId = "5e7f2156783c1655d6255a94";
        final Professor oldProfessor = professorService.findRawById(validId);
        assertNotNull(oldProfessor);
        final String oldName = oldProfessor.getName();
        final String newName = "New Name";

        oldProfessor.setName(newName);
        Professor updatedProfessor = professorService.updateRaw(oldProfessor);
        assertNotNull(updatedProfessor);
        assertEquals(updatedProfessor.getName(), newName);
        oldProfessor.setName(oldName);
        updatedProfessor = professorService.updateRaw(oldProfessor);
        assertNotNull(updatedProfessor);
        assertEquals(updatedProfessor.getName(), oldName);
    }

    @Test
    void update() {
        final String validId = "5e7f2156783c1655d6255a94";
        final Professor oldProfessor = professorService.findRawById(validId);
        assertNotNull(oldProfessor);
        final String oldName = oldProfessor.getName();
        final String newName = "New Name";

        oldProfessor.setName(newName);
        ProfessorPV updatedProfessorPV = professorService.update(oldProfessor);
        assertNotNull(updatedProfessorPV);
        assertEquals(updatedProfessorPV.getName(), newName);
        oldProfessor.setName(oldName);
        updatedProfessorPV = professorService.update(oldProfessor);
        assertNotNull(updatedProfessorPV);
        assertEquals(updatedProfessorPV.getName(), oldName);
    }

    @Test
    void delete() {
        final Professor validProfessor =
                Professor.builder().name("New Prof").build();
        final Professor invalidProfessor = new Professor();
        final ProfessorPV newProfessor = professorService.add(validProfessor);
        assertNotNull(newProfessor);
        assertTrue(professorService.delete(newProfessor.toRawType()));
        assertFalse(professorService.delete(invalidProfessor));
        assertFalse(professorService.delete(null));
    }
}