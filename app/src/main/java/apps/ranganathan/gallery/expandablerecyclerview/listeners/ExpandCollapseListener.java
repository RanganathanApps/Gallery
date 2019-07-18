package apps.ranganathan.gallery.expandablerecyclerview.listeners;


public interface ExpandCollapseListener {

  /**
   * Called when a group is expanded
   *
   */
  void onGroupExpanded(int positionStart, int itemCount);

  /**
   * Called when a group is collapsed
   *
   */
  void onGroupCollapsed(int positionStart, int itemCount);
}
