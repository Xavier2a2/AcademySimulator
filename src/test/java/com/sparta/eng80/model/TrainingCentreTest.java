package com.sparta.eng80.model;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class TrainingCentreTest {

    @Test
    @DisplayName("Testing to see if unique Trainees can be created")
    public void createTrainingCentre() {
        TrainingCenter trainingCenter = new TrainingCenter("Test");
        Assertions.assertNotNull(trainingCenter);
    }

    @Test
    @DisplayName("Testing to see if trainees can be added")
    public void addTraineesToTrainingCentre() {
        TrainingCenter trainingCenter = new TrainingCenter("Test");
        trainingCenter.addToTrainingCentre(new Trainee("Trainee 1"));
        Assertions.assertTrue(trainingCenter.getInTraining().size() > 0);
    }

    @Test
    @DisplayName("Testing to see if null trainees can be added")
    public void addNullTraineeTest() {
        TrainingCenter trainingCenter = new TrainingCenter("Test");
        Assertions.assertFalse(trainingCenter.addToTrainingCentre(null));
    }

    @Test
    @DisplayName("Testing to see if duplicate trainees can be added")
    public void addDuplicateTraineeTest() {
        TrainingCenter trainingCenter = new TrainingCenter("Test");
        Trainee trainee = new Trainee("Drake");
        trainingCenter.addToTrainingCentre(trainee);
        Assertions.assertFalse(trainingCenter.addToTrainingCentre(trainee));
    }

    @Test
    @DisplayName("Testing to see if more then 100 trainees can be added")
    public void addMoreThen100TraineeTest() {
        TrainingCenter trainingCenter = new TrainingCenter("Test");
        boolean added = true;
        for (int i = 0; i < 110; i++) {
            added = trainingCenter.addToTrainingCentre(
                    new Trainee("" + i)
            );
            if (!added) {
                break;
            }
        }
        Assertions.assertFalse(added);
    }
}
