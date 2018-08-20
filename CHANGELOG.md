# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.4.0] - 2018-08-20
### Added
- This changelog!
- A little documentation (readme).
- US translations for items and configuration GUI.
- Update version file for forge update checker.
- Wither skeleton boss event.
- Debug renderer for ongoing events (when using F3).

### Changed
- Compatibility level to Minecraft v1.12
- Improved spawning logic for mobs (better probability for a successful spawn).
- Event cool down configuration unit from seconds to minutes.
- Event message only shows two events (and the number of additional events active).

### Removed
- Copies of vanilla tool textures (`shovel`, `axe`, `pickaxe`, `hoe`); they are not needed anymore.
- Copies of vanilla tool recipes (`shovel`, `axe`, `pickaxe`, `hoe`); they are not needed anymore.
- Giant boss event, because it has no AI.
- "rampage"-AI to make monsters more aggressive during events (because monsters are now aggressive enough in vanilla).
- Some obsolete and experimental code which was never active or working.

### Fixed
- Reaching of event kill limit not triggering event ending.
- Event message now scales in height depending on the amount of text shown.
 
## 0.3.3 - 2013-09-22
### Added
- adventure-pickaxe, which lasts 3 times longer and decreases working speed with damage.
- adventure-axe, which lasts 3 times longer and decreases working speed with damage.
- adventure-hoe, which lasts 5 times longer and decreases working speed with damage.
- adventure-shovel, which lasts 4 times longer and decreases working speed with damage.
- adventure-sledge, which has 1/6 durability of the pickaxe and is used to destroy patches of rock at once.

## 0.3.2 - 2013-09-10
### Added
- Aggro range of monsters (see known issues) is now set to range of specific event.
- "rampage"-AI to make monsters more aggressive during events.
- Close events are now also shown in multi player.

### Changed
- Replaced `startevent` and `stopevent` commands with a more general event command.
- Replaced invasion spawn radius with min and max spawn radius.

### Fixed
- On invasions monsters spawn around the player between min and max spawn radius (before monsters could spawn next to the player).

## 0.3.1 - 2013-09-06
### Added
- Close events are now shown in single player mode.
- Boss events including adds.
- Plug & play event system for custom events!

### Changed
- Integration of invasions into new event system.
- Monsters cannot spawn in normal rain (on the surface).

### Fixed
- Monsters now spawn with gear!
- Monsters won't spawn in bright areas anymore (lightlevel > 10).

## 0.3.0 - 2013-09-04
### Changed
- Tweaked monster spawn chances (reduced pigmen, raised skeletons and zombies).
- Better algorithm to detect invasion occasion (less CPU hungry).

### Fixed
- Monsters will not stick halfway in the ground anymore.
- Monsters now spawn on the ground and not on player level in mid air.

## 0.2.0 - 2013-09-03
### Added
- Invasions!
- Configuration file for invasions.

[Unreleased]: https://github.com/rampage128/adventuremode/compare/0.4.0-beta...HEAD
[0.4.0]: https://github.com/rampage128/adventuremode/compare/0.3.3-beta...0.4.0-beta