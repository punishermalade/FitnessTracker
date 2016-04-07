package com.punisher.fitnesstracker.validator;

import com.punisher.fitnesstracker.R;
import com.punisher.fitnesstracker.dto.FitnessActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Encapsulate the validation when creating a new activity
 */
public class FitnessValidator {

    private HashMap<String, Integer> _validations = null;
    private FitnessActivity _fitness = null;

    public FitnessValidator() {
        _validations = new HashMap<String, Integer>();
    }

    public boolean hasErrors() {
        return _validations.keySet().size() > 0;
    }

    public HashMap<String, Integer> validate(FitnessActivity a) {
        _validations.clear();
        _fitness = a;

        validateDay();
        validateTime();
        validateFitnessType();
        validateDuration();
        validateDuration();

        _validations.clear();

        return _validations;
    }

    private void validateDay() {

        Date now = new Date(System.currentTimeMillis());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);

        Calendar fitCalendar = new GregorianCalendar();
        fitCalendar.setTime(_fitness.getDayOfActivity());

        if (fitCalendar.before(calendar)) {
            _validations.put("Day", R.string.errors_day_in_future);
        }
    }

    private void validateTime() {
        // no validation on the time, some blind trust here
    }

    private void validateDuration() {
        if (_fitness.getDuration() <= 0) {
            _validations.put("Duration", R.string.errors_duration_is_zero);
        }
    }

    private void validateFitnessType() {
        for (FitnessActivity.FitnessType ft : FitnessActivity.FitnessType.values()) {
            if (ft == _fitness.getFitnessType()) {
                return;
            }
        }
        _validations.put("FitnessType", R.string.errors_fitness_type_not_valid);
    }

    private void validateDistance() {
        if (_fitness.getDistance() <= 0) {
            _validations.put("Distance", R.string.errors_distance_is_zero);
        }
    }

}
