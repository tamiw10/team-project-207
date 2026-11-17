package use_case.suggest_idea;

/** The output boundary for the Suggest Idea Use Case. */
public interface SuggestIdeaOutputBoundary {

  /**
   * Prepares the success view: the day's ideas with the new idea included.
   *
   * @param outputData the output data
   */
  void prepareSuccessView(SuggestIdeaOutputData outputData);

  /**
   * Prepares the failure view.
   *
   * @param errorMessage the explanation of the failure
   */
  void prepareFailView(String errorMessage);
}
