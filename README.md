# PaginatorRenderKit

**currently the library is in development, but anyway you can use it's source code in your projects**

Paginator Render Kit is a set of abstractions to simplify and speed up implementation of paginatable lists with different view types.

The library is based on solutions developed by:
* Konstantin Tskhovrebov aka terrakok, see [source code of the GitFox client](https://gitlab.com/terrakok/gitlab-client) and [Konstantin's standup at Yandex Academy](https://www.youtube.com/watch?v=h5afEeuI0GQ) about Paginator itself
* Hannes Dorfmann aka sockeqwe, see [the Adapter Delegates library](https://github.com/sockeqwe/AdapterDelegates) and [Hannes's article about the library](http://hannesdorfmann.com/android/adapter-delegates)

### Dependencies
* Step 1. Add jitpack repository to your root `build.gradle` file:
```
allprojects {
   repositories {
      ...
      maven { url 'https://jitpack.io' }
   }
}
```
* Step 2. Add dependency in application module `build.gradle` :
```
dependencies {
   implementation 'com.github.DeMoss15:PaginatorRenderKit:$paginatorRenderKitVersion'
}
```


# [License](https://github.com/DeMoss15/PaginatorRenderKit/blob/master/LICENSE)
```
Copyright 2019 Daniel Mossur

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
