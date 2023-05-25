# Changelog
All notable changes to this project will be documented in this file.

## [2.1.14](https://github.com/inbrainai/sdk-android/releases/tag/2.1.14) - 2023-05-25

### Fixed
- Force fire callback even if client/secret is invalid
- Fix some parameter casting issues
---

## [2.1.13](https://github.com/inbrainai/sdk-android/releases/tag/2.1.13) - 2023-05-22

### Fixed
- Fixed an issue parsing parameters of `getNativeSurveys` request
---

## [2.1.12](https://github.com/inbrainai/sdk-android/releases/tag/2.1.12) - 2023-05-16

### Fixed
- Update surveys availability checking logic
- Refactor and cleanup codes
---

## [2.1.6](https://github.com/inbrainai/sdk-android/releases/tag/2.1.6) - 2023-02-13

### Fixed
- Fixed an issue accessing Kotlin code from C#
---

## [2.1.5](https://github.com/inbrainai/sdk-android/releases/tag/2.1.5) - 2023-01-30

### Added/Fixed
- Include a sub module example for easier test purpose.
- Remove useless logic regarding confirmedRewardsIds and lastReceivedRewards.
- Make didReceiveInBrainRewards() optional.
- Adjust default toolbar and statusbar configs.
- Make it possible to init InBrain outside main thread.
---

## [2.1.2](https://github.com/inbrainai/sdk-android/releases/tag/2.1.2) - 2023-01-03

### Added/Fixed
- Added option to setup SDK without `userId`.
- Added option to set `userId` separately from SDK setup.
- Added info about survey's conversion level.
- Added option to confirm rewards by transactionIds.
- Added option to setup `sessionId` and `dataOptions` separately.
- Added new callback with information about reward for NativeSurveys.
- Deprecated some old functions and properties.
---

## [2.0.0](https://github.com/inbrainai/sdk-android/releases/tag/2.0.0) - 2022-10-30

### Added/Fixed
- Added support for categories.
- Improved usage of native surveys methods.
---

## [1.0.28](https://github.com/inbrainai/sdk-android/releases/tag/1.0.28) - 2022-10-24

### Fixed
- Fixed the proguard rule.
---

## [1.0.27](https://github.com/inbrainai/sdk-android/releases/tag/1.0.27) - 2022-09-13

### Fixed
- Changed the redirect URL and dialog content when clicking back button to exit a survey.
---

## [1.0.26](https://github.com/inbrainai/sdk-android/releases/tag/1.0.26) - 2022-09-05

### Fixed
- Show a loading indicator when the survey WebView is being initialized.
---

## [1.0.25](https://github.com/inbrainai/sdk-android/releases/tag/1.0.25) - 2022-07-03

### Added
- Added support for profile match. Each survey now has `conversionThreshold` field which reflects conversion rates.
---

## [1.0.24](https://github.com/inbrainai/sdk-android/releases/tag/1.0.24) - 2022-06-15

### Added
- Added support for some specific apps to fetch ongoing currency sale details.
---

## [1.0.23](https://github.com/inbrainai/sdk-android/releases/tag/1.0.23) - 2022-05-31

### Added/Fixed
- Getters for 2 InBrain properties (`sessionUid` and `dataOptions`) have been exposed for convenience.
- Upgraded AGP to latest and added `maven-publish` plugin to resolve some JitPack build warnings.
---

## [1.0.21](https://github.com/inbrainai/sdk-android/releases/tag/1.0.21) - 2021-12-13

### Added
- 2 additional properties (`currencySale` and `multiplier`) have become available for native surveys.
---

## [1.0.20](https://github.com/inbrainai/sdk-android/releases/tag/1.0.20) - 2021-11-24

### Fixed
- Fixed the issue where we were often falling into a blank screen when trying to enter an inBrain survey.
---

## [1.0.19](https://github.com/inbrainai/sdk-android/releases/tag/1.0.19) - 2021-10-04

### Fixed
- Enabled `setLanguage()` back.
---

## [1.0.18](https://github.com/inbrainai/sdk-android/releases/tag/1.0.18) - 2021-09-23

### Added
- Add support for passing in an optional parameter `placement_id` to the native surveys APIs.
---

## [1.0.16](https://github.com/inbrainai/sdk-android/releases/tag/1.0.16) - 2020-01-21

### Added
- Add support for multiple windows for webview.
---

## [1.0.15](https://github.com/inbrainai/sdk-android/releases/tag/1.0.15) - 2021-01-12

### Added
- Added support for nativeSurveyClosed message.
---

## [1.0.14](https://github.com/inbrainai/sdk-android/releases/tag/1.0.14) - 2020-12-11

### Fixed
- Inverted light status bar icons color.
---

## [1.0.13](https://github.com/inbrainai/sdk-android/releases/tag/1.0.13) - 2020-12-11
 
### Added
- Navigation to feedback after aborting survey.

## [1.0.12](https://github.com/inbrainai/sdk-android/releases/tag/1.0.12) - 2020-12-03

### Added
- inBrain V2 customization.
---

## [1.0.9](https://github.com/inbrainai/sdk-android/releases/tag/1.0.9) - 2020-11-02

### Added
- Added native surveys.
---
