# discogs-app
Android application that interacts with the Discogs API to search for artists, display detailed information, and navigate the artist’s releases.

## Git hooks
Run the command below once after cloning to enable repository-managed hooks:

```bash
./gradlew installGitHooks
```

After hooks are installed, every `git push` runs `ktlintCheck` and `detekt`. Pushes are blocked when either task reports violations.
