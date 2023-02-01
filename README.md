# jmix-menu-editor
[![Check](https://github.com/daring2/jmix-menu-editor/actions/workflows/check.yml/badge.svg)](https://github.com/daring2/jmix-menu-editor/actions/workflows/check.yml)

## Overview
The add-on provides support of multiple main menus in Jmix applications. Menus are stored in the database
and can be created and edited at runtime using the administration screens.

## Installation
Install the add-on using the following coordinates `'io.github.it-syn:jmix-menu-editor-starter:<add-on version>'`
according [the platform documentation](https://docs.jmix.io/jmix/studio/marketplace.html).

The latest version is: [0.8.5](https://central.sonatype.com/artifact/io.github.it-syn/jmix-menu-editor/0.8.5)

## Usage
Menus can be created and edited in the "Main menus" screen in the Administration menu. Menu attributes (name, description, etc)
and items can be changed in the "Menu editor" screen. Items can be reordered using Drag & Drop function.

![image](https://user-images.githubusercontent.com/11490702/211078842-3d3eba44-fb3a-4a4d-a98f-54ef3dcf8b95.png)

![image](https://user-images.githubusercontent.com/11490702/211079118-cd1b182c-2b75-4c1f-974d-dd84076fb764.png)

![image](https://user-images.githubusercontent.com/11490702/211079299-80e010db-22ae-46fd-8e6e-7fcc36c28843.png)

The Reset action in the "Menu editor" screen restores menu items from the default menu `menu.xml`.

The menu can be assigned to a user role using the "Role code" attribute. After that it is applied for all users
that have this role. If multiple menus are applied via different roles to the user the menu with
the highest Priority attribute is used.
