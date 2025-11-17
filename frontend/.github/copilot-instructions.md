## Repo context

This is the frontend Angular application for the luthierflow project (generated with Angular CLI v20). The app is a single-page Angular application using the standalone component API (see `src/main.ts` and `src/app/app.ts`). The codebase is small and intentionally minimal.

Key files to inspect when making changes:
- `src/main.ts` — bootstraps the app using `bootstrapApplication(AppComponent, { providers: [...] })` and registers router and HTTP provider.
- `src/app/app.ts` — root component (standalone-style). Uses `signal()` for component state.
- `src/app/app.routes.ts` — router `Routes` (currently empty array). Add routes here when adding new pages/components.
- `src/app/app.config.ts` — application provider configuration (error listeners, zone change detection, router).
- `angular.json`, `package.json` — build/serve/test configuration and npm scripts.

Primary workflows and exact commands
- Start dev server (hot reload): `npm start` (runs `ng serve`). App served at `http://localhost:4200/`.
- Build production: `npm run build` (runs `ng build`). Output goes to `dist/` per Angular config.
- Run unit tests: `npm test` (runs `ng test` with Karma).
- VS Code debug: there are launch configurations that run `npm: start` or `npm: test` as preLaunchTask. See `.vscode/launch.json` and `.vscode/tasks.json`.

Project-specific conventions and patterns
- Standalone components API: files use the new Angular standalone bootstrap (`bootstrapApplication`) and standalone component definitions (no NgModule). Look at `src/main.ts` and `src/app/app.ts`.
- Global providers are declared via `appConfig` (`src/app/app.config.ts`) or directly in `bootstrapApplication`. Prefer centralizing application-wide providers in `app.config.ts` when possible.
- Routing: `src/app/app.routes.ts` exports `Routes`. When adding a route, update both the routes file and `appConfig` or `main.ts` providers depending on where you register the router.
- HTTP: `provideHttpClient()` is used in `main.ts`. Use Angular's HttpClient APIs from `@angular/common/http` for backend calls.
- Styling: global styles in `src/styles.scss`; component styles are SCSS by default (see `angular.json` schematics config).

Code patterns to follow (specific examples)
- When adding a new page component, create a standalone component, export it, and add its route to `src/app/app.routes.ts`. Example route entry:

  { path: 'instruments', loadComponent: () => import('./instruments/instruments.component').then(m => m.InstrumentsComponent) }

- Use `signal()` for simple reactive state in components (example: `protected readonly title = signal('frontend')` in `src/app/app.ts`).
- Register cross-cutting providers (error listeners, zone change detection) in `src/app/app.config.ts`.

Testing and CI notes
- Unit tests use Karma and Jasmine (see `package.json`): `npm test` runs `ng test` which uses the `test` target in `angular.json`.
- There is no e2e framework preconfigured. If adding e2e tests, include the configuration and scripts.

Integration and external dependencies
- Backend API calls should use `HttpClient` and environment config can be added in a conventional `src/environments/` folder if needed (not present currently).
- Dependencies are in `package.json` (Angular v20, RxJS, zone.js). Dev tooling uses `@angular/cli` and Karma.

What an AI agent should do first
1. Run `npm install` locally (if dependencies are missing) and `npm start` to confirm the dev server boots.
2. Inspect `src/main.ts`, `src/app/app.config.ts`, and `src/app/app.routes.ts` to understand where to register providers and routes.
3. When modifying bootstrapping or providers, keep `provideBrowserGlobalErrorListeners()` and `provideZoneChangeDetection(...)` present in `app.config.ts` unless the change explicitly replaces them.

Edge cases and guardrails
- Don’t introduce NgModule-based code—this project uses standalone components exclusively.
- Keep styles in SCSS. The project schematics and build expect `scss` files.
- Avoid modifying `.vscode` configs unless you know they are for local developer convenience (used by VS Code launch/tasks).

If anything in this file is unclear or you need deeper architectural details (backend routes, environments, or larger app structure), ask and I will inspect additional files or branches.
