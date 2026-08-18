# katastrof2.0
# 🌋 DisasterPanel - Advanced Disaster Management Plugin

**DisasterPanel** is a powerful admin panel plugin for **Purpur 1.21.4** that allows server administrators to trigger and control natural disasters with a beautiful futuristic GUI.

![DisasterPanel Banner](https://via.placeholder.com/800x200/1a1a2e/ff6b35?text=DisasterPanel+v1.0.0)

## ✨ Features

### 🎮 Beautiful GUI Interface
- Futuristic neon design with glassmorphism style
- 3 separate control panels for each disaster type
- Real-time status indicators
- Intuitive controls

### 🌍 Three Disaster Types

#### ⚡ Earthquake
- **7 intensity levels** (1-7)
- **Progressive effects**: 
  - Levels 1-4: Vibrations and sound
  - Levels 5-7: Fissures appear
- Configurable radius and duration
- Visual particle effects

#### ☄️ Meteorite
- **Drop from Y=2000** with realistic physics
- **Progressive sound effects** based on height:
  - 2000-1500: Low sound
  - 1500-500: Medium sound
  - 500-0: MAX volume
- **Impact effects**:
  - Crater creation
  - 60-second fire duration
  - 60-second danger zone with damage

#### 🌋 Volcano
- **Lava rise and fall** mechanics
- **Obsidian formation** when lava cools
- **Re-eruption trigger**: Break obsidian to restart
- Particle effects: Magma, smoke, fire

### ⚙️ Configuration
- Full YAML configuration
- Adjustable radii, durations, and heights
- Customizable particle density
- Sound volume control

## 📋 Requirements

- **Server**: Purpur 1.21.4
- **Java**: 21 or higher
- **Permissions**: `disasterpanel.admin`

## 🚀 Installation

1. Download the latest JAR from [Releases](https://github.com/yourusername/DisasterPanel/releases)
2. Place the JAR in your server's `plugins/` folder
3. Restart your server or use `/reload`
4. Configure `plugins/DisasterPanel/config.yml` if needed
5. Use `/disaster` to open the control panel

## 🎮 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/disaster` | Open main control panel | `disasterpanel.admin` |
| `/disaster stop` | Stop all active disasters | `disasterpanel.admin` |

## 🛠️ Building from Source

### Prerequisites
- Java 21
- Maven 3.6+

### Build Steps

```bash
# Clone the repository
git clone https://github.com/yourusername/DisasterPanel.git
cd DisasterPanel

# Build with Maven
mvn clean package

# Find the JAR in target/
ls target/DisasterPanel-*.jar
