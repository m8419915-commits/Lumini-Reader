import React from 'react';
import { LuminaProvider, useLumina } from './context/LuminaContext';
import { BottomNavBar } from './components/navigation/BottomNavBar';
import { SnapSwitcherModal } from './components/snap/SnapSwitcherModal';

// Screens
import { HomeDashboardScreen } from './components/screens/HomeDashboardScreen';
import { LibraryScreen } from './components/screens/LibraryScreen';
import { ExploreScreen } from './components/screens/ExploreScreen';
import { UpdatesScreen } from './components/screens/UpdatesScreen';
import { HistoryScreen } from './components/screens/HistoryScreen';
import { MoreScreen } from './components/screens/MoreScreen';
import { MangaDetailScreen } from './components/screens/MangaDetailScreen';
import { HighPerformanceReaderScreen } from './components/screens/HighPerformanceReaderScreen';
import { MangaUniverseMapScreen } from './components/screens/MangaUniverseMapScreen';
import { MangaDnaAchievementsScreen } from './components/screens/MangaDnaAchievementsScreen';
import { LuminaForgeScreen } from './components/screens/LuminaForgeScreen';
import { LuminaMutationScreen } from './components/screens/LuminaMutationScreen';
import { LuminaAiAssistantScreen } from './components/screens/LuminaAiAssistantScreen';
import { LuminaJourneyMapScreen } from './components/screens/LuminaJourneyMapScreen';
import { MangaTimelineScreen } from './components/screens/MangaTimelineScreen';
import { BackupSyncScreen } from './components/screens/BackupSyncScreen';
import { SettingsScreen } from './components/screens/SettingsScreen';
import { RepositoriesScreen } from './components/screens/RepositoriesScreen';
import { MigrationScreen } from './components/screens/MigrationScreen';

const MainContent: React.FC = () => {
  const {
    screen,
    activeToast,
  } = useLumina();

  const renderScreen = () => {
    switch (screen.type) {
      case 'home':
        return <HomeDashboardScreen />;
      case 'library':
        return <LibraryScreen />;
      case 'explore':
        return <ExploreScreen />;
      case 'updates':
        return <UpdatesScreen />;
      case 'history':
        return <HistoryScreen />;
      case 'more':
        return <MoreScreen />;
      case 'detail':
        return <MangaDetailScreen mangaId={screen.mangaId || 1} />;
      case 'reader':
        return (
          <HighPerformanceReaderScreen
            mangaId={screen.mangaId || 1}
            chapterId={screen.chapterId || 101}
            initialPage={screen.initialPage || 0}
          />
        );
      case 'universe':
        return <MangaUniverseMapScreen mangaId={screen.mangaId} />;
      case 'dna':
        return <MangaDnaAchievementsScreen />;
      case 'forge':
        return <LuminaForgeScreen />;
      case 'mutation':
        return <LuminaMutationScreen />;
      case 'ai':
        return <LuminaAiAssistantScreen />;
      case 'journey':
        return <LuminaJourneyMapScreen />;
      case 'timeline':
        return <MangaTimelineScreen mangaId={screen.mangaId} />;
      case 'backup':
        return <BackupSyncScreen />;
      case 'settings':
        return <SettingsScreen />;
      case 'repositories':
        return <RepositoriesScreen />;
      case 'migration':
        return <MigrationScreen />;
      default:
        return <HomeDashboardScreen />;
    }
  };

  const showBottomNav = ['home', 'library', 'explore', 'updates', 'history', 'more'].includes(screen.type);

  return (
    <div className="min-h-screen bg-black text-white font-sans selection:bg-[#8B5CF6]/30 selection:text-white">
      {renderScreen()}

      {/* Snap Switcher Modal */}
      <SnapSwitcherModal />

      {/* Persistent Bottom Navigation */}
      {showBottomNav && <BottomNavBar />}

      {/* Global Toast Notification */}
      {activeToast && (
        <div className="fixed top-5 left-1/2 -translate-x-1/2 z-50 px-4 py-2.5 rounded-2xl bg-[#18181E]/90 backdrop-blur-md border border-[#8B5CF6]/50 text-white text-xs font-semibold shadow-[0_0_20px_rgba(139,92,246,0.3)] animate-fade-in flex items-center gap-2 pointer-events-none">
          <span className="w-2 h-2 rounded-full bg-[#00E5FF] animate-ping" />
          {activeToast}
        </div>
      )}
    </div>
  );
};

export function App() {
  return (
    <LuminaProvider>
      <MainContent />
    </LuminaProvider>
  );
}

export default App;
