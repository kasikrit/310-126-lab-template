import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * One-time helper for 310-126 OOP: moves the work of an old one-task repo (310-126-lab0) into a task folder,
 * so the repo becomes the single course repo 310-126-oop. Only for students who made 310-126-lab0 before.
 *
 * Usage (same in Windows PowerShell and macOS Terminal), in the root folder of your repo:
 *   gh repo clone kasikrit/310-126-lab-template ../310-126-template
 *   java ../310-126-template/Migrate.java Lab0
 * Optional second argument: the folder of the cloned template (default ../310-126-template).
 *
 * What it does:
 *   1. checks this is a git repo with no uncommitted changes and no Lab0/ yet;
 *   2. moves every tracked file and folder at the root (except README.md, .gitignore, .gitattributes and
 *      dot-folders) into Lab0/ with `git mv`, so their history follows them;
 *   3. copies NewTask.java, _template/, README.md, .gitignore and .gitattributes from the template.
 * It does not commit. Afterwards:  git add -A   git commit -m "Move Lab 0 into Lab0/"   git push
 */
public class Migrate {
    static final Set<String> KEEP_AT_ROOT = Set.of("README.md", ".gitignore", ".gitattributes");
    static final List<String> FROM_TEMPLATE = List.of("NewTask.java", "_template", "README.md", ".gitignore", ".gitattributes");

    public static void main(String[] args) throws Exception {
        if (args.length < 1 || args.length > 2) {
            System.out.println("Usage: java ../310-126-template/Migrate.java Lab0 [template folder, default ../310-126-template]");
            System.exit(2);
        }
        String name = args[0].replaceAll("[/\\\\]+$", "");
        Path root = Path.of("").toAbsolutePath();
        Path tpl = root.resolve(args.length == 2 ? args[1] : "../310-126-template").normalize();

        if (!name.matches("[A-Za-z][A-Za-z0-9_-]*")) fail("'" + name + "' is not a valid folder name, e.g. Lab0");
        if (!Files.isDirectory(root.resolve(".git"))) fail("run this in the root folder of your repo (the folder that has .git)");
        if (!Files.isDirectory(tpl.resolve("_template")) || !Files.isRegularFile(tpl.resolve("NewTask.java")))
            fail("template not found at " + tpl + "\n  first run:  gh repo clone kasikrit/310-126-lab-template ../310-126-template");
        if (Files.exists(root.resolve("_template")) || Files.exists(root.resolve(name)))
            fail("this repo already has _template/ or " + name + "/; it looks migrated already, nothing was changed");
        if (!git(root, "status", "--porcelain").isBlank())
            fail("you have uncommitted changes; commit and push them first (git add .  git commit -m \"...\"  git push)");

        List<String> move = new ArrayList<>();
        for (String entry : git(root, "ls-tree", "--name-only", "HEAD").split("\n")) {
            if (!entry.isBlank() && !KEEP_AT_ROOT.contains(entry) && !entry.startsWith(".")) move.add(entry);
        }
        if (move.isEmpty()) fail("no tracked files to move at the root of this repo");

        Files.createDirectories(root.resolve(name));
        for (String entry : move) {
            git(root, "mv", entry, name + "/" + entry);
            System.out.println("moved   " + entry + "  ->  " + name + "/" + entry);
        }
        for (String f : FROM_TEMPLATE) {
            Path src = tpl.resolve(f);
            if (Files.exists(src)) {
                copyTree(src, root.resolve(f));
                System.out.println("added   " + f + (Files.isDirectory(src) ? "/" : ""));
            }
        }
        System.out.println();
        System.out.println("Done, nothing committed yet. Check with  git status  then:");
        System.out.println("  git add -A");
        System.out.println("  git commit -m \"Move Lab 0 into " + name + "/\"");
        System.out.println("  git push");
        System.out.println("From now on: work inside " + name + "/ (cd " + name + "), and start a new task with  java NewTask <folder>");
    }

    static String git(Path dir, String... args) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<>(List.of("git"));
        cmd.addAll(List.of(args));
        Process p = new ProcessBuilder(cmd).directory(dir.toFile()).redirectErrorStream(true).start();
        String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        if (p.waitFor() != 0) fail("git " + String.join(" ", args) + " failed:\n" + out);
        return out;
    }

    static void copyTree(Path from, Path to) throws IOException {
        if (Files.isRegularFile(from)) {
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            return;
        }
        try (Stream<Path> s = Files.walk(from)) {
            for (Path p : s.sorted().toList()) {
                Path dest = to.resolve(from.relativize(p).toString());
                if (Files.isDirectory(p)) Files.createDirectories(dest);
                else Files.copy(p, dest, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    static void fail(String msg) {
        System.out.println("Migrate: " + msg);
        System.exit(2);
    }
}
