package use_case.suggest_idea;

/** Input Boundary for suggesting a new activity idea. */
public interface SuggestIdeaInputBoundary {

  /**
   * Executes the suggest idea use case.
   *
   * @param suggestIdeaInputData the input data
   */
  void execute(SuggestIdeaInputData suggestIdeaInputData);
}
