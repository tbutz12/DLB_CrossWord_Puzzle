
public class DLB implements DictInterface {

  private static class Nodelet {

    public char letter;
    public Nodelet sibling;
    public Nodelet child;
    public boolean isWord;

    public Nodelet() {

      letter = '\0';
      sibling = null;
      child = null;
      isWord = false;

    }
  }
  public Nodelet root;

  public DLB() {
    root = null;
  }

  public boolean add(String s) {
    //if root is null, set it to the first letter of the word being passed in
    //MUST HANDLE 3 CASES....
    //Case 1: root doesn't contain a word yet, empty DLB
    //Case 2: root is the 1st letter of another word, but will need a sibling
    //for a different word
    //Case 3: root does not equal the first letter of another word, need to
    //make a sibling to root
    Nodelet curr = root;
    Nodelet parent = root;
    for (int x = 0; x < s.length(); x++) {
      if (curr == null) {
        curr = new Nodelet();
        if(parent != null){
          parent.child = curr;
        }
        parent = curr;
        if(x == 0){
          root = curr;
        }
        curr.letter = s.charAt(x);
        if(x == s.length() -1){
          curr.isWord = true;
        }
        curr = curr.child;
      }
      else if (curr.letter == s.charAt(x)) {
        if(x == s.length() -1){
          curr.isWord = true;
        }
        parent = curr;
        curr = curr.child;
      }
      else{
        Nodelet temp = curr;
        Nodelet newNode = new Nodelet();
        while (temp.sibling != null && temp.sibling.letter != s.charAt(x)) {
          temp = temp.sibling;
        }
        if(temp.sibling == null){
          temp.sibling = newNode;
          newNode.letter = s.charAt(x);
          if(x == s.length() -1){
            temp.sibling.isWord = true;
          }
          curr = newNode.child;
          parent = newNode;
        }
        else{
          if(x == s.length() -1){
             temp.sibling.isWord = true;
          }
          curr = temp.sibling.child;
          parent = temp.sibling;
        }
      }
    }
    return true;
  }

  public int searchPrefix(StringBuilder s) {
    return searchPrefix(s, 0, s.length() - 1);
  }

  public int searchPrefix(StringBuilder s, int start, int end) {
    Nodelet cur = root;
    if(cur == null){
      return 0;
    }
    for(int x = 0; x < s.length(); x++){
      while(s.charAt(x) != cur.letter){
        if(cur.sibling != null){
          cur = cur.sibling;
        }
        else{
          return 0;
        }
      }
      if(x != end){
        if(cur.child != null){
          cur = cur.child;
        }
        else{
          return 0;
        }
      }
    }
    if(cur.isWord == true && cur.child != null){
      return 3;
    }
    else if(cur.isWord == true && cur.child == null){
        return 2;
    }
    else if(cur.isWord == false && cur.child != null){
      return 1;
    }
    else{
      return 0;
    }
  }
}
/*
  private void print(Nodelet root){
    Nodelet temp = root;
    while(temp != null){
      System.out.println(temp.letter);
      print(temp.child);
      temp = temp.sibling;
    }

  }
  public void print(){
    print(root);
  }

}
*/
