# /docs

This directory will contain documentation that pertains to the project as a whole, or to the git repository itself.
If you are ever stuck, or want to know something about git processes, or need to know something about the project
in particular, you may find the solution here.

The bulk of the information will be contained in this file, but niche topics may be diverted to external `.md` documents.
The full index of available topics will be included in this file.

## Git routines

When working with git, it is important that a certain pattern is followed in order to minimize the number of conflicts
that can emerge when working from multiple histories. The general rule is to try to stay as synchronized as possible,
problems only generally arise when two or more members complete large pieces of work on divergent histories.

### General rule

The trick is to keep the work small, do a local change that pertains to _one_ topic at a time. Once the change is done,
you may `git add .` and `git commit -m 'message...'`. After the commit, you want to synchronize with the remote. This
is generally done using `git pull`, however, it is recommended to first verify whether there is any work waiting to be
pulled from the remote. This can be done using `git fetch --all`.

### git fetch

Fetch differs from pull, in that it does not modify the working directory, meaning it is a lot safer to run than `git pull`.
After the fetch, you should check the status of git (using a live view, or the `git status` command) to check if there
are any changes that need to be pulled. If none are present, feel free to just `git push` to the remote.

### Pulling from the remote

If there is work waiting on the remote, you may need to `pull`. The advantage of running `git fetch` before the pull, is
that you can get a preview of how far behind you are from the remote. If the histories diverge by a large number of commits,
it may be recommended to contact the dev of the latest commits to work out the merge of histories.

#### fast-forward

Once you want to pull from the remote, there are two general ways to merge your work with the remote work. The first way is
to _fast-forward_. Fast-forward can only be done if you are directly behind the remote history and just want to get up to
date with the latest commit. This is generally the safest option, and should be done whenever possible. You can fast-forward
by running the command: `git pull --ff-only`

#### rebase

The second way of merging your work with the work on the remote, is to _rebase_. Rebase is just another form of _merge_ that
instead of starting a new commit for the merge itself, `git rebase` will try to merge your commit history into the remote branch.
This means that the branch tree itself is cleaner, however it also means it is riskier, as it overwrites history. Changes made by
`git rebase` can therefore be _**unrecoverable**_ in some instances (mostly a problem when rebasing from external branches).

It is still however recommended to use `git pull --rebase` (`git fetch` + `git rebase`) instead of merging the two histories using
`git merge` because it keeps the history cleaner.

`git rebase` is just basically another form of `git merge` meaning you will need to resolve merge conflicts. These are typically
easiest to manage through your IDE, however you can also manage them through git by manually staging the changes you want and
(`git add`) and then running `git rebase --continue`.

You can always abort a conflicting rebase using `git rebase --abort`. If the rebase continues without prompting, it means the merge
could occur without conflict, meaning you should have nothing to worry about.

## Testing

When testing, it is important to remember that the purpose of the test is to prove that the application behaves the way it is expected
to behave. Therefore it is important that tests are written with that idea in mind, as tests that do the opposite often cause devs
to make wrong assuptions about the state of the application.

### Unit tests - design philosophy

Each test should be small, and predictable. Having to debug a test is not an ideal circumstance, as it means that not only do you not
know the state of the application, the tool used to test the applications ready-state is faulty. If you can, write the test for only
a single small case, and write it to be only a few lines long.

Unit tests are also run by default when building the project, so it is important that the test is _reproducible_. That means that
for the next build of the application, given that the code the unit test tested is the same, the test should always show the same
result as the previous run.

Unit tests can be thought of as a part of the build process itself. Should the test fail, it should be treated in the same way a
compilation error is treated: refactor the code until the test passes.

### Integration testing

Integration testing is the joint test of a group of components in the application. This is typically done as a test that can be
separately launched whenever a certain real-time aspect of the application is to be tested.

Integration tests can be longer than unit tests, and may require specific setups, but the quality of the test is still important.
If an integration test fails, that should tell us that the application is not ready for release, not that the test is faulty. For
this reason, it is recommended that the tests start off simple, and grow in complexity as more and more aspects of the application
are ready for release.

In general, you should only integration test code that has been unit tested, and complex integration tests should only be run on
code that has been integration tested using simple tests.

### System testing

System testing is testing the system as a whole. This will typically be done last, and consists of running the application in it's
entirety and testing all aspects at once. For us, this will be launching the front-end and back-end at once and attempting to run the
entire site through a browser on the local machine.
