<p align="center">
  <img id="Infinitum Framework" src="http://infinitumframework.com/images/infinitum.jpg" />
</p>

Overview
--------

**Infinitum** is an extensible, modular framework enabling Android developers to quickly and efficiently create rich, domain-driven applications while facilitating the convention-over-configuration paradigm. One of its primary goals is to foster a strong separation of concerns, empowering developers to write modular, cohesive, and concise software for the Android platform. The idea is to maintain focus on the fundamental business problem of the software, not the underlying plumbing that connects it.

Infinitum is currently in a pre-release form and is still under active development. See the road map for what's in store for the framework.

Infinitum Core
--------------

[Infinitum Core](https://github.com/InfinitumFramework/Infinitum-Core) offers an inversion-of-control container which allows for framework and non-framework beans to be injected and retrieved at runtime. Beans, aspects, and other application components can be automatically discovered by Infinitum, and the framework also provides support for autowiring properties, methods, and constructors. Likewise, Android activities can be injected with layouts, views, and resources -- yielding cleaner, more concise code.

In addition to dependency injection, Infinitum provides a lightweight logging framework which wraps Android's Logcat. This logging framework allows log statements to be made within application code but only asserted in debug environments. This means that logging code does not need conditional statements or be removed altogether when preparing an application for release.

Infinitum ORM
-------------

Infinitum includes an [object-relational mapper](https://github.com/InfinitumFramework/Infinitum-ORM) that allows developers to spend more time focusing on their problem domain and core business logic and less time on innate data-access and boilerplate code. It embraces object-oriented principles such as polymorphism, inheritance, and association while maintaining a great deal of flexibility. The ORM allows developers to specify what is transient or persistent at a class- and field-level, and it is configurable using either XML mappings or Java annotations. The Infinitum ORM also provides a criteria API for constructing database queries, allowing developers to query on objects rather than tables -- no SQL necessary.

The framework also offers an extensible REST ORM implementation, granting developers an effortless way to communicate with their own RESTful web services using domain objects.

Infinitum Web
-------------

Through its [Web module](https://github.com/InfinitumFramework/Infinitum-Web), Infinitum offers a REST client, which provides support for caching, simplified authentication, and a number of other features that allow developers to consume REST APIs with ease. The framework offers both fine- and coarse- grained clients, meaning developers can choose to deal with HTTP responses at a low level (i.e. manually performing message interpretation, deserialization, etc.) or a high level (i.e. allowing the framework to handle message conversion).

Infinitum AOP
-------------

In order to separate cross-cutting concerns, Infinitum implements an [aspect-oriented programming framework](https://github.com/InfinitumFramework/Infinitum-AOP). With it, developers can alter or extend the behavior of core application code by creating aspects, which are used to apply advice at specific join points.

Infinitum AOP also includes a cache abstraction. When enabled, methods can be marked for caching so that results can be retrieved without invoking the method on subsequent calls with the same arguments. This is particularly valuable for computation- or resource- intensive code.
