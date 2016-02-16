package com.insuranceline.utils;

import com.insuranceline.data.vo.Goal;

import java.util.List;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 12,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class CampaignAlgorithm {

    private static final float DIFFICULTY_SCALE_FOR_SECOND_GOAL = 5f;//1.01f;
    private static final float DIFFICULTY_SCALE_FOR_THIRD_GOAL  = 5f;//1.015f;

    private static final float LEFT_DAY_SCALE_FOR_SECOND_GOAL = 0.4f;

    public static List<Goal> startGoal(long newGoalId,int bias,  List<Goal> goalList, long endOfCampaignDate){

        Goal goal1 = getGoalById(0, goalList);
        Goal goal2 = getGoalById(1, goalList);
        Goal goal3 = getGoalById(2, goalList);

        long dayLef = TimeUnit.MILLISECONDS.toDays(endOfCampaignDate - System.currentTimeMillis());

        Timber.d("Day Left to end of campain: %s", dayLef);

        // First Goal
        if (newGoalId == 0){
            goal1.setBaseDate(System.currentTimeMillis());
            goal1.setStatus(Goal.GOAL_STATUS_ACTIVE);

            //set steps bias
            goal1.setStepsBias(bias);
        }
        // Second Goal
        else if(newGoalId == 1){
            float stepPerDayPrevGoal = goal1.getTarget() / goal1.getAchievedInDays();
            float dayLeftForGoal2    = dayLef * LEFT_DAY_SCALE_FOR_SECOND_GOAL;
            float newStepPerDay      = stepPerDayPrevGoal * DIFFICULTY_SCALE_FOR_SECOND_GOAL;
            float newTarget          = dayLeftForGoal2 * newStepPerDay;

            Timber.d("GOAL 2 ==> stepPerDayPrevGoal: %s, dayLeftForGoal2: %s, newStepPerDay: %s, newTarget: %s",
                    stepPerDayPrevGoal, dayLeftForGoal2, newStepPerDay, newTarget);

            // target
            goal2.setTarget((long)newTarget);

            // base time
            goal2.setBaseDate(System.currentTimeMillis());

            // Daily Requirement
            goal2.setRequiredDailySteps((long)newStepPerDay);
            goal2.setRequiredDailyActiveMin(goal1.getRequiredDailyActiveMin());
            goal2.setRequiredDailyCalorie(goal1.getRequiredDailyCalorie());
            goal2.setRequiredDailyDistance(goal1.getRequiredDailyDistance());

            // set Status
            goal2.setStatus(Goal.GOAL_STATUS_ACTIVE);

            //set steps bias
            goal2.setStepsBias(bias);
        }
        // Third Goal
        else if (newGoalId == 2){
            float stepPerDayPrevGoal = goal2.getTarget() / goal2.getAchievedInDays();
            float dayLeftForGoal3    = dayLef ;
            float newStepPerDay      = stepPerDayPrevGoal * DIFFICULTY_SCALE_FOR_THIRD_GOAL;
            float newTarget          = dayLeftForGoal3 * newStepPerDay;

            Timber.d("GOAL 2 ==> stepPerDayPrevGoal: %s, dayLeftForGoal3: %s, newStepPerDay: %s, newTarget: %s",
                    stepPerDayPrevGoal, dayLeftForGoal3, newStepPerDay, newTarget);

            // target
            goal3.setTarget((long)newTarget);

            // base time
            goal3.setBaseDate(System.currentTimeMillis());

            // Daily Requirement
            goal3.setRequiredDailySteps((long)newStepPerDay);
            goal3.setRequiredDailyActiveMin(goal2.getRequiredDailyActiveMin());
            goal3.setRequiredDailyCalorie(goal2.getRequiredDailyCalorie());
            goal3.setRequiredDailyDistance(goal2.getRequiredDailyDistance());

            // set Status
            goal3.setStatus(Goal.GOAL_STATUS_ACTIVE);

            //set steps bias
            goal3.setStepsBias(bias);
        }

        return goalList;
    }

    public static int calculateNextTarget(long newGoalId, List<Goal> goalList, long endOfCampaignDate ){

        Goal goal1 = getGoalById(0, goalList);
        Goal goal2 = getGoalById(1, goalList);
        Goal goal3 = getGoalById(2, goalList);

        long dayLef = TimeUnit.MILLISECONDS.toDays(endOfCampaignDate - System.currentTimeMillis());

        Timber.d("Day Left to end of campain: %s", dayLef);
        // First Goal
        if (newGoalId == 0){
            return (int)goal1.getTarget();
        }
        // Second Goal
        else if(newGoalId == 1){
            float stepPerDayPrevGoal = goal1.getTarget() / goal1.getAchievedInDays();
            float dayLeftForGoal2    = dayLef * LEFT_DAY_SCALE_FOR_SECOND_GOAL;
            float newStepPerDay      = stepPerDayPrevGoal * DIFFICULTY_SCALE_FOR_SECOND_GOAL;
            float newTarget          = dayLeftForGoal2 * newStepPerDay;

            Timber.d("GOAL 2 ==> stepPerDayPrevGoal: %s, dayLeftForGoal2: %s, newStepPerDay: %s, newTarget: %s",
                    stepPerDayPrevGoal, dayLeftForGoal2, newStepPerDay, newTarget);
            return  (int)newTarget;

        }
        // Third Goal
        else if (newGoalId == 2){
            float stepPerDayPrevGoal = goal2.getTarget() / goal2.getAchievedInDays();
            float dayLeftForGoal3    = dayLef ;
            float newStepPerDay      = stepPerDayPrevGoal * DIFFICULTY_SCALE_FOR_THIRD_GOAL;
            float newTarget          = dayLeftForGoal3 * newStepPerDay;

            Timber.d("GOAL 2 ==> stepPerDayPrevGoal: %s, dayLeftForGoal3: %s, newStepPerDay: %s, newTarget: %s",
                    stepPerDayPrevGoal, dayLeftForGoal3, newStepPerDay, newTarget);

            return  (int)newTarget;
        } else {
            throw new RuntimeException("No Goal Defined for this id: " + newGoalId);
        }


    }

    public static List<Goal> endGoal(long endingGoalId, List<Goal> goalList){
        Goal goal1 = getGoalById(0, goalList);
        Goal goal2 = getGoalById(1, goalList);
        Goal goal3 = getGoalById(2, goalList);

        // First Goal
        if (endingGoalId == 0){
            goal1.setEndDate(System.currentTimeMillis());
            goal1.setStatus(Goal.GOAL_STATUS_DONE);
            goal2.setStatus(Goal.GOAL_STATUS_IDLE);
        } else if(endingGoalId == 1){
            goal2.setEndDate(System.currentTimeMillis());
            goal2.setStatus(Goal.GOAL_STATUS_DONE);
            goal3.setStatus(Goal.GOAL_STATUS_IDLE);
        } else if (endingGoalId == 2){
            goal3.setEndDate(System.currentTimeMillis());
            goal3.setStatus(Goal.GOAL_STATUS_DONE);
        }

        return goalList;

    }

    private static Goal getGoalById(long newGoalId, List<Goal> goalList) {

        Goal res = goalList.get(0);

        for(Goal goal : goalList){
            if (goal.getGoalId() == newGoalId){
                res = goal;
                break;
            }
        }

        return res;
    }
}