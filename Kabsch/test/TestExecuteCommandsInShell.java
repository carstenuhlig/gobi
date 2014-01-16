import util.ExecuteShellCommand;

/**
 * Created by uhligc on 15.01.14.
 */
public class TestExecuteCommandsInShell {
    public static void main(String[] args) {
        System.out.println(ExecuteShellCommand.executeCommand("ll"));
    }
}
