package org.wisc.business.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wisc.business.model.BusinessModel.Comment;
import org.wisc.business.model.PVModels.CommentPV;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CommentServiceTest {
    @Autowired
    CommentService commentService;

    @Test
    void findRawById() {
        final String validId = "5e7fb90f6e1ea30d75993711";
        final String validIdMessage = "Test with a valid id";
        final String invalidId = "5e7fb90f6e1ea30d75993732";
        final String invalIdMessage = "Test with invalid id";
        assertNotNull(commentService.findRawById(validId), validIdMessage);
        assertNull(commentService.findRawById(invalidId), invalIdMessage);
        assertNotNull(commentService.findRawById(null));
    }

    @Test
    void findById() {
        final String validId = "5e7fb90f6e1ea30d75993711";
        final String validIdMessage = "Test with a valid id";
        final String invalidId = "5e7fb90f6e1ea30d75993732";
        final String invalIdMessage = "Test with invalid id";

        final CommentPV validIdResult = commentService.findById(validId);
        assertNotNull(validIdResult, validIdMessage);
        assertEquals(validIdResult.toRawType(),
                commentService.findRawById(validId));

        final CommentPV invalidIdResult = commentService.findById(invalidId);
        assertNull(invalidIdResult, invalIdMessage);
        assertNull(commentService.findById(null));
    }

    @Test
    void all() {
        List<CommentPV> allComments = commentService.all();
        assertNotNull(allComments, "Test all comments");
        assertTrue(allComments.size() > 1, "Test non-empty comment list");
    }

    @Test
    void add() {
        final Comment validComment = Comment.builder().authorId(
                "5e8fef0c96635a65deaa4eb8").termId("5e7f12300b0e027580738658").build();
        final String validCommentMessage = "Test with valid comment";
        final Comment inValidComment = new Comment();
        final String inValidCommentMessage = "Test with invalid comment";
        final CommentPV rValid = commentService.add(validComment);
        assertNotNull(rValid, validCommentMessage);
        assertEquals(rValid.toRawType(), validComment);

        // delete the test comment
        assertTrue(commentService.delete(rValid.toRawType()));

        assertNull(commentService.add(inValidComment), inValidCommentMessage);
        assertNull(commentService.add(null));
    }

    @Test
    void update() {
        final CommentPV validComment = commentService.findById(
                "5e903aec94585272f2bfd505");
        final CommentPV inValidComment = commentService.findById("123432");

        assertNotNull(validComment, "Test update valid comment not null");
        assertNull(inValidComment, "Test update invalid comment null");

        double oldRating = validComment.getRating();
        double newRating = oldRating;
        while (oldRating == newRating) {
            newRating = (Math.random() * 5.0);
        }
        validComment.setRating(newRating);
        final CommentPV updatedComment =
                commentService.update(validComment.toRawType());

        assertNotNull(updatedComment, "Test updated comment");
        assertEquals(validComment.getId(), updatedComment.getId());
        assertEquals(validComment.getRating(), updatedComment.getRating());

        // change the rating back
        validComment.setRating(oldRating);
        CommentPV revertedCommentPV =
                commentService.update(validComment.toRawType());
        assertNotNull(revertedCommentPV);
        assertNotEquals(revertedCommentPV.getRating(), updatedComment.getRating());
        assertNull(commentService.update(null));
    }

    @Test
    void delete() {
        final Comment validComment = Comment.builder().authorId(
                "5e8fef0c96635a65deaa4eb8").termId("5e7f12300b0e027580738658").build();
        final Comment invalidComment = new Comment();
        final CommentPV newCommentPV = commentService.add(validComment);
        assertNotNull(newCommentPV);
        assertTrue(commentService.delete(newCommentPV.toRawType()));
        assertFalse(commentService.delete(newCommentPV.toRawType()));
        assertFalse(commentService.deleteRaw(invalidComment));
        assertFalse(commentService.delete(null));
    }
}