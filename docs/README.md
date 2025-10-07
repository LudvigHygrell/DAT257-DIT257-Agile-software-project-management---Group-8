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

The second way of merging your work with the work on the remote, is to _rebase_, meaning perform the 