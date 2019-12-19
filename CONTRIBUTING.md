# Contributing Guidelines

Thank you for your interest in contributing to our project. Whether it's a bug report, new feature, correction, or additional documentation, we greatly value feedback and contributions from our community.

Please read through this document before submitting any issues or pull requests to ensure we have all the necessary information to effectively respond to your contribution or bug report.

## Contributing via Pull Requests

_Note: ONLY applicable to [InnerSource REPO for EMS-Platform](https://tfsemea1.ta.philips.com/tfs/TPC_Region02/Innersource/_git/mobile-plf-android)

To send us a pull request, please:

1. Clone the repository and create a branch based off of dev
2. The Naming convention for the branch to be submitted should be <innersource/[bugfix/feature/spike]/[name]>.
    * e.g. `innersource/feature/fancy_animations` or `innersource/bugfix/crash_on_my_device`
3. Modify the source on the branch
    * We are strong believers in Clean Code, but please focus on the specific change you are contributing!If you also reformat code, e.g. apply the 'boyscout principle', it will be hard for us to focus on your change.
    * If you have structural change suggestions: please create a separate branch, and describe the intention for your changes in the pull request.
4. Ensure that local build and required test cases pass.
5. After completing the work, Rebase and then push the changes to Forked repository ( git push origin <your branch>).
6. Create Pull request to [review and merge code]( https://docs.microsoft.com/en-us/vsts/git/pull-requests?view=vsts#from-the-code-view-on-the-web).
7. In Reviewers field it is Mandatory to add CDP2_Innersource@philips.com as code reviewer.
8. Please note that the minimum number of reviewers is 2.
9. Mail will be sent to you after the changes from your branch are merged to master branch.
10.For any concern, please contact to IET Mobile Platform (Functional Account) <iet_mobileplatform@philips.com>