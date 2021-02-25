package com.sparta.eng80.controller;

import com.sparta.eng80.model.Trainee;
import com.sparta.eng80.model.TrainingCentre;
import com.sparta.eng80.model.types_of_centres.Bootcamp;
import com.sparta.eng80.model.types_of_centres.TechCentre;
import com.sparta.eng80.model.types_of_centres.TrainingHub;
import com.sparta.eng80.util.Date;
import com.sparta.eng80.util.RandomGenerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TrainingCentreManager {

    private int numberOfBootcamps = 0;
    private int numberOfTechCentres = 0;
    private int numberOfTrainingHub = 0;
    private Date startDate;
    private Date currentDate;
    private Date lastCentreAddedDate;
    private ArrayList<TrainingCentre> listOfTrainingCentres = new ArrayList<>();
    private RandomGenerator randomGenerator = new RandomGenerator(8923478235482354823L);
    private TraineeManager traineeManager;


    public TrainingCentreManager(Date startDate) {
        this.startDate = lastCentreAddedDate = startDate;
    }

    public void randomlyGenerateCentre(Date currentDate) {
        int num1 = numberOfBootcamps < 2 ? 1 : 2;
        int choice = randomGenerator.inRange(num1, 3);
        switch (choice) {
            case 1:
                generateNewBootcamp(currentDate);
                break;
            case 2:
                generateNewTechCentre(currentDate);
                break;
            case 3:
                generateNewTrainingHub(currentDate);
                break;
        }

    }

    public void generateNewBootcamp(Date currentDate) {
        String bootcampName = "Boot Camp " + numberOfBootcamps;
        this.currentDate = currentDate;
        if (currentDate.isEqual(lastCentreAddedDate.plusMonths(BigInteger.TWO)) || currentDate.isEqual(startDate)) {
            TrainingCentre bootcamp = new Bootcamp(bootcampName, currentDate);
            numberOfBootcamps++;
            lastCentreAddedDate = currentDate;
            listOfTrainingCentres.add(bootcamp);
        }
    }

    public void generateNewTechCentre(Date currentDate) {
        String techCentreName = "Tech Centre " + numberOfTechCentres;
        this.currentDate = currentDate;
        if (currentDate.isEqual(lastCentreAddedDate.plusMonths(BigInteger.TWO)) || currentDate.isEqual(startDate)) {
            TechCentre techCentre = new TechCentre(techCentreName, currentDate);
            numberOfTechCentres++;
            lastCentreAddedDate = currentDate;
            listOfTrainingCentres.add(techCentre);
        }
    }

    public void generateNewTrainingHub(Date currentDate) {
        String techCentreName = "Tech Centre " + numberOfTrainingHub;
        this.currentDate = currentDate;
        if (currentDate.isEqual(lastCentreAddedDate.plusMonths(BigInteger.TWO)) || currentDate.isEqual(startDate)) {
            TrainingCentre trainingHub = new TrainingHub(techCentreName, currentDate);
            numberOfTrainingHub++;
            lastCentreAddedDate = currentDate;
            listOfTrainingCentres.add(trainingHub);
        }
    }

    public ArrayList<TrainingCentre> getListOfTrainingCenters() {
        return listOfTrainingCentres;
    }

    public int getNumOfFullTrainingCentres() {
        int count = 0;
        for (TrainingCentre trainingCentre : listOfTrainingCentres) {
            if (trainingCentre.getSize() == trainingCentre.MAX_SIZE) {
                count++;
            }
        }
        return count;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void checkCentreAges() {
        //For all the training centres, checks if the age is x months old and the centre is open
        for (TrainingCentre trainingCentre : listOfTrainingCentres) {
            if ((trainingCentre.getAge() == 3 && !trainingCentre.isClosed && trainingCentre.getClass().getName().contains("Boot"))||
                    (trainingCentre.getAge() == 2 && !trainingCentre.isClosed && !trainingCentre.getClass().getName().contains("Boot"))) {
                //If so the size is checked. If below 25 the centre is closed and trainees are reallocated
                if (trainingCentre.getInTraining().size() < 25) {
                    trainingCentre.isClosed = true;
                    List<Trainee> trainees = trainingCentre.clearTrainees();
                    reallocateTraineesRandomly(trainees);
                }
            }
        }
    }

    private void updateTrainingCentres() {
        for (TrainingCentre trainingCentre : listOfTrainingCentres) {
            trainingCentre.updateInfo();
        }
    }


    private void reallocateTraineesRandomly(List<Trainee> trainees) {
        List<TrainingCentre> availableCentres = new ArrayList<>();
        for (TrainingCentre trainingCentre : listOfTrainingCentres) {
            if (trainingCentre.getInTraining().size() != trainingCentre.MAX_SIZE || !trainingCentre.isClosed) {
                availableCentres.add(trainingCentre);
            }
        }
        while (availableCentres.size() > 0 && trainees.size() > 0) {
            int centreSelection = randomGenerator.inRange(0, availableCentres.size());
            for (int i = 0; i < availableCentres.get(centreSelection).spacesAvailable; i++) {
                if (availableCentres.get(centreSelection).getInTraining().size() < availableCentres.get(centreSelection).MAX_SIZE) {  //redundant?
                    if(!availableCentres.get(centreSelection).getClass().getName().contains("Tech")) {
                        availableCentres.get(centreSelection).addTrainee(trainees.remove(i));
                    } else { //If its a Tech Centre
                        TechCentre techCentre = (TechCentre) availableCentres.get(centreSelection);
                        boolean allocated = false;
                        for (int j=0; j<trainees.size();j++){
                            if(!allocated){
                                if(trainees.get(i).getCourseType() == techCentre.getCourseType()){
                                    availableCentres.get(centreSelection).addTrainee(trainees.remove(i));
                                    allocated = true;
                                }
                            }
                        }
                    }
                }
            }
            availableCentres.remove(centreSelection);
            if(trainees.size()>0){
                traineeManager.addToWaitingList(trainees);
            }
        }
    }

}
