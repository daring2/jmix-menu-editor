# jmix-menu-editor

## Overview
The add-on provides support of multiple main menus in Jmix applications. Menus are stored in the database
and can be created and edited at runtime using the administration screens.

## Installation
Install the add-on using the following coordinates `'ru.itsyn.jmix:jmix-menu-editor-starter:<add-on version>'`
according [the platform documentation](https://docs.jmix.io/jmix/studio/marketplace.html).

The latest version is: [0.8.3](TODO)

## Usage
Menus can be created and edited in "Main menus" screen in Administration menu. Menu attributes (name, description, etc) and items can be changed in "Menu editor" screen. Items can be reordered
using Drag & Drop function.

TODO screenshots

Reset action in "Menu editor" screen restores menu items from the default menu `menu.xml`.

The menu can be assigned to a user role using Role attribute. After that it is applied for all users
that have this role. If multiple menus via different roles are applied to the user the menu with
the highest priority is used.
