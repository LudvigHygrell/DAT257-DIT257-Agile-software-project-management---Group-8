# BeneSphere Theme System Guide

## 🎨 Overview

The BeneSphere application now uses a **centralized CSS theming system** with CSS Custom Properties (CSS Variables). This makes it incredibly easy to change colors across the entire application and supports dark mode out of the box.

---

## 📁 File Structure

```
client/src/styles/
├── themes.css          ← **Central theme file** (all colors defined here)
├── App.css
├── Navbar.css
├── CharityList.css
├── AccountSettings.css
├── UserMenu.css
└── ... (all other CSS files)
```

---

## 🚀 Quick Start

### How to Change Colors

**Want to change the entire app's color scheme?** Just edit `themes.css`!

**Example:** Change the primary blue color:
```css
/* In themes.css */
:root {
  --color-primary: #538AF7;  /* Change this line! */
}
```

That's it! The navbar, buttons, and all primary-colored elements will update automatically.

---

## 📖 Color Variables Reference

### Brand Colors
| Variable | Default | Usage |
|----------|---------|-------|
| `--color-primary` | `#538AF7` | Main blue - navbar, primary buttons |
| `--color-primary-hover` | `#4a7cd9` | Darker blue for hover states |
| `--color-primary-light` | `#7AB4E8` | Light blue for subtle accents |
| `--color-secondary` | `#81D5C4` | Teal/turquoise accent |
| `--color-success` | `#28a745` | Green for success states |
| `--color-danger` | `#dc3545` | Red for danger/delete actions |
| `--color-accent-purple` | `#667eea` | Purple for avatars, special buttons |

### Background Colors
| Variable | Default | Usage |
|----------|---------|-------|
| `--bg-app` | `#f5f6fa` | Main app background |
| `--bg-panel` | `#D9F0E7` | Panel/card container (light green) |
| `--bg-card` | `#ffffff` | White card background |
| `--bg-hover` | `#f5f5f5` | Hover state background |

### Text Colors
| Variable | Default | Usage |
|----------|---------|-------|
| `--text-primary` | `#1a1a1a` | Headings, important text |
| `--text-secondary` | `#333` | Body text |
| `--text-tertiary` | `#666` | Muted, less important text |
| `--text-light` | `#ffffff` | White text |

### Shadows
| Variable | Usage |
|----------|-------|
| `--shadow-sm` | Small shadow for cards |
| `--shadow-md` | Medium shadow |
| `--shadow-lg` | Large shadow for panels |
| `--shadow-xl` | Extra large shadow for hover effects |

---

## 🌙 Dark Mode

### How to Enable Dark Mode

Dark mode is already defined in `themes.css`! To activate it, add this attribute to the HTML element:

```javascript
// In your JavaScript/React code
document.documentElement.setAttribute('data-theme', 'dark');
```

### How to Toggle Dark Mode

Here's a simple toggle function you can add to your app:

```javascript
function toggleDarkMode() {
  const currentTheme = document.documentElement.getAttribute('data-theme');
  const newTheme = currentTheme === 'dark' ? 'light' : 'dark';

  // Update the theme
  document.documentElement.setAttribute('data-theme', newTheme);

  // Save preference
  localStorage.setItem('theme', newTheme);
}

// Load saved theme on app start
const savedTheme = localStorage.getItem('theme') || 'light';
document.documentElement.setAttribute('data-theme', savedTheme);
```

### Where to Add the Toggle Button

The dark mode toggle is already in the UserMenu component! Just connect it to the toggle function above by updating `UserMenu.jsx`:

```javascript
// In UserMenu.jsx
const handleDarkModeToggle = () => {
  const currentTheme = document.documentElement.getAttribute('data-theme');
  const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
  document.documentElement.setAttribute('data-theme', newTheme);
  localStorage.setItem('theme', newTheme);
};

// In the JSX, find the dark mode button and update it:
<button className="dropdown-item" onClick={handleDarkModeToggle}>
  {/* ... icon ... */}
  <span>Dark Mode</span>
  <div className="toggle-switch">
    <input
      type="checkbox"
      id="dark-mode-toggle"
      checked={document.documentElement.getAttribute('data-theme') === 'dark'}
      onChange={handleDarkModeToggle}
    />
    <label htmlFor="dark-mode-toggle"></label>
  </div>
</button>
```

---

## 🎯 Common Use Cases

### 1. Change the Entire Color Scheme

Want a red theme instead of blue? Edit `themes.css`:

```css
:root {
  --color-primary: #e74c3c;         /* Red instead of blue */
  --color-primary-hover: #c0392b;   /* Darker red */
  --color-primary-light: #e67e73;   /* Light red */
}
```

### 2. Change Panel Background Color

```css
:root {
  --bg-panel: #E8F4F8;  /* Change from green to light blue */
}
```

### 3. Adjust Success Green Color

```css
:root {
  --color-success: #4CAF50;       /* Different shade of green */
  --color-success-dark: #388E3C;  /* Darker version */
}
```

### 4. Create a Custom Theme (e.g., Ocean Theme)

```css
:root {
  --color-primary: #006994;        /* Ocean blue */
  --color-secondary: #00C9A7;      /* Teal */
  --bg-app: #F0F8FF;               /* Alice blue background */
  --bg-panel: #E0F2F7;             /* Light cyan */
  --color-success: #00A86B;        /* Jade green */
}
```

---

## 🔧 How to Use Variables in CSS

When writing or updating CSS files, always use variables instead of hardcoded colors:

### ❌ Don't Do This:
```css
.my-button {
  background-color: #538AF7;
  color: #ffffff;
}
```

### ✅ Do This:
```css
.my-button {
  background-color: var(--color-primary);
  color: var(--text-light);
}
```

### Benefits:
- Changes instantly when theme is updated
- Works automatically with dark mode
- Consistent colors across the app

---

## 📝 Migration Status

### ✅ All Files Migrated!
All CSS files in the project now use the centralized theme system:

- `themes.css` - **Created** with all color variables and dark mode support
- `main.jsx` - **Updated** to import themes.css first
- `App.css` - **Migrated** to use CSS variables
- `Navbar.css` - **Migrated** to use CSS variables
- `CharityList.css` - **Migrated** and cleaned up duplicates
- `AccountSettings.css` - **Migrated** to use CSS variables
- `UserMenu.css` - **Migrated** to use CSS variables
- `LoginModal.css` - **Migrated** to use CSS variables
- `Registration.css` - **Migrated** to use CSS variables
- `CharityPage.css` - **Migrated** to use CSS variables
- `CommentSection.css` - **Migrated** to use CSS variables
- `NewCharityPage.css` - **Migrated** to use CSS variables

🎉 **The entire application now supports theming and is ready for dark mode!**

---

## 🎨 Creating New Themes

### Example: Blue Corporate Theme

1. Edit `themes.css` and update the `:root` section:

```css
:root {
  /* Blue corporate theme */
  --color-primary: #0066CC;
  --color-primary-hover: #0052A3;
  --color-primary-light: #3385D6;

  --bg-app: #F5F7FA;
  --bg-panel: #E8EDF4;
  --bg-card: #FFFFFF;

  --color-success: #28A745;
  --color-danger: #DC3545;
}
```

2. Save the file
3. Refresh the browser - **done!**

---

## 🛠️ Troubleshooting

### Colors Not Changing?

1. **Clear your browser cache** - CSS files might be cached
2. **Check if themes.css is imported first** in `main.jsx`
3. **Verify you're using `var(--variable-name)`** syntax, not hardcoded colors
4. **Check browser DevTools** to see which color is being applied

### Dark Mode Not Working?

1. Check if `data-theme="dark"` attribute is on the `<html>` element
2. Verify dark mode colors are defined in `[data-theme="dark"]` section in `themes.css`
3. Make sure all CSS files are using variables, not hardcoded colors

---

## 📚 Best Practices

1. **Always use CSS variables** for colors - never hardcode hex values
2. **Test both light and dark themes** when making changes
3. **Use semantic variable names** - `--color-primary` is better than `--blue-color`
4. **Keep themes.css organized** - group related colors together
5. **Document custom themes** - add comments explaining color choices

---

## 🚀 Next Steps

### Recommended Enhancements:

1. ✅ ~~Migrate remaining CSS files~~ - **COMPLETED!**
2. **Implement dark mode toggle** in UserMenu component (code provided above)
3. **Add theme persistence** with localStorage (code provided above)
4. **Create additional theme presets** (e.g., high contrast, colorblind-friendly)
5. **Test thoroughly** on all pages in both light and dark modes
6. **Fine-tune dark mode colors** based on user feedback

---

## 💡 Tips

- **Use browser DevTools** to test color changes live before updating themes.css
- **Create theme presets** for different brands or seasons
- **Consider accessibility** - ensure sufficient color contrast (WCAG AA standard)
- **Test with colorblind simulators** to ensure usability for all users

---

## 📞 Support

For questions or issues with the theme system, check:
- **themes.css** - All color definitions
- **Browser DevTools** - Inspect which CSS variable is being applied
- **This guide** - Common solutions and examples

---

**Happy Theming! 🎨**
