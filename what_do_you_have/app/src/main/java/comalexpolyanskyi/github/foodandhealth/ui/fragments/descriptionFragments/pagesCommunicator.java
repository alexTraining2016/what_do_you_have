package comalexpolyanskyi.github.foodandhealth.ui.fragments.descriptionFragments;

public interface PagesCommunicator<T> {

    void updateData(T data);

    void setupColor(int color);
}
