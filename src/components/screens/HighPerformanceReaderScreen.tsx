import React, { useState, useEffect, useRef } from 'react';
import {
  ArrowLeft,
  Settings2,
  ChevronLeft,
  ChevronRight,
  Sun,
  ZoomIn,
  Zap,
  Sparkles,
  Columns,
  AlignVerticalSpaceAround,
  BookOpen,
  Eye,
  Sliders,
  Check,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { ReaderMode, BackgroundTint } from '../../types';

interface HighPerformanceReaderScreenProps {
  mangaId: number;
  chapterId: number;
  initialPage?: number;
}

export const HighPerformanceReaderScreen: React.FC<HighPerformanceReaderScreenProps> = ({
  mangaId,
  chapterId,
  initialPage = 0,
}) => {
  const {
    getManga,
    getChaptersForManga,
    readerConfig,
    updateReaderConfig,
    saveSnap,
    goBack,
    navigate,
    markChapterRead,
    addHistoryItem,
  } = useLumina();

  const manga = getManga(mangaId) || getManga(1)!;
  const chapters = getChaptersForManga(manga.id);
  const currentChapter = chapters.find(c => c.id === chapterId) || chapters[0];

  const [currentPage, setCurrentPage] = useState<number>(initialPage);
  const [controlsVisible, setControlsVisible] = useState<boolean>(true);
  const [showSettingsDrawer, setShowSettingsDrawer] = useState<boolean>(false);
  const [ambientGlowColor, setAmbientGlowColor] = useState<string>('rgba(139, 92, 246, 0.35)');

  const pages = currentChapter.pages;
  const totalPages = pages.length;

  const webtoonContainerRef = useRef<HTMLDivElement>(null);

  // Auto-save Lumina Snap state when page/chapter changes
  useEffect(() => {
    saveSnap({
      mangaId: manga.id,
      title: manga.title,
      coverUrl: manga.thumbnailUrl,
      chapterId: currentChapter.id,
      chapterNumber: currentChapter.chapterNumber,
      pageIndex: currentPage,
      totalPages: totalPages,
      progressPercent: (currentPage + 1) / totalPages,
    });

    addHistoryItem({
      mangaId: manga.id,
      mangaTitle: manga.title,
      coverUrl: manga.thumbnailUrl,
      chapterId: currentChapter.id,
      chapterNumber: currentChapter.chapterNumber,
      chapterTitle: currentChapter.title,
      pageIndex: currentPage,
      totalPages: totalPages,
      readAt: Date.now(),
      progressPercent: (currentPage + 1) / totalPages,
    });

    // Dynamic Lumina Flow color shifting simulation
    const colors = [
      'rgba(0, 229, 255, 0.4)',
      'rgba(139, 92, 246, 0.45)',
      'rgba(244, 63, 94, 0.4)',
      'rgba(245, 158, 11, 0.4)',
      'rgba(16, 185, 129, 0.4)',
    ];
    setAmbientGlowColor(colors[currentPage % colors.length]);

    if (currentPage >= totalPages - 1) {
      markChapterRead(currentChapter.id, true);
    }
  }, [currentPage, currentChapter.id, manga.id]);

  // Navigate to adjacent chapters
  const currentChapterIndex = chapters.findIndex(c => c.id === currentChapter.id);
  const nextChapter = currentChapterIndex > 0 ? chapters[currentChapterIndex - 1] : null;
  const prevChapter = currentChapterIndex < chapters.length - 1 ? chapters[currentChapterIndex + 1] : null;

  // Background tint classes
  const getBackgroundClass = () => {
    switch (readerConfig.backgroundTint) {
      case BackgroundTint.PITCH_BLACK:
        return 'bg-black';
      case BackgroundTint.CHARCOAL:
        return 'bg-[#0E0E12]';
      case BackgroundTint.SEPIA:
        return 'bg-[#1C1612]';
      case BackgroundTint.DEEP_SLATE:
        return 'bg-[#0B0F19]';
      default:
        return 'bg-black';
    }
  };

  // Handle click on center panel to toggle controls
  const handlePanelClick = (e: React.MouseEvent) => {
    const width = window.innerWidth;
    const clickX = e.clientX;

    // In single page LTR / RTL, clicking edges changes pages
    if (readerConfig.readerMode === ReaderMode.SINGLE_PAGE_LTR) {
      if (clickX < width * 0.3) {
        // Prev page
        if (currentPage > 0) setCurrentPage(currentPage - 1);
        return;
      } else if (clickX > width * 0.7) {
        // Next page
        if (currentPage < totalPages - 1) setCurrentPage(currentPage + 1);
        return;
      }
    } else if (readerConfig.readerMode === ReaderMode.SINGLE_PAGE_RTL) {
      if (clickX < width * 0.3) {
        // Next page in RTL
        if (currentPage < totalPages - 1) setCurrentPage(currentPage + 1);
        return;
      } else if (clickX > width * 0.7) {
        // Prev page in RTL
        if (currentPage > 0) setCurrentPage(currentPage - 1);
        return;
      }
    }

    // Toggle controls on center click
    setControlsVisible(!controlsVisible);
  };

  return (
    <div
      className={`relative min-h-screen ${getBackgroundClass()} text-white select-none overflow-x-hidden flex flex-col items-center`}
      style={{
        filter: `brightness(${readerConfig.brightness}%)`,
      }}
    >
      {/* LUMINA FLOW: Dynamic Ambient Immersion Backlighting */}
      {readerConfig.enableLuminaFlow && (
        <div
          className="fixed inset-0 pointer-events-none z-0 transition-all duration-700 ease-out blur-[90px] opacity-60"
          style={{
            background: `radial-gradient(circle at 50% 50%, ${ambientGlowColor} 0%, transparent 70%)`,
          }}
        />
      )}

      {/* TOP CONTROLS OVERLAY */}
      <div
        className={`fixed top-0 left-0 right-0 z-40 bg-black/90 backdrop-blur-md border-b border-[#27272A]/70 px-4 py-3 flex items-center justify-between transition-transform duration-300 ${
          controlsVisible ? 'translate-y-0' : '-translate-y-full'
        }`}
      >
        <div className="flex items-center gap-3">
          <button
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6]"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div className="min-w-0">
            <h3 className="text-xs font-bold text-white truncate max-w-[200px] sm:max-w-md">
              {manga.title}
            </h3>
            <p className="text-[11px] text-[#A78BFA] font-semibold flex items-center gap-1.5">
              Ch. {currentChapter.chapterNumber} • {currentChapter.title}
            </p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          {/* Lumina Flow Indicator Pill */}
          <button
            onClick={() => updateReaderConfig({ enableLuminaFlow: !readerConfig.enableLuminaFlow })}
            className={`px-2.5 py-1 rounded-xl text-xs font-bold flex items-center gap-1.5 transition-all ${
              readerConfig.enableLuminaFlow
                ? 'bg-[#8B5CF6]/20 border border-[#8B5CF6] text-[#A78BFA] shadow-[0_0_10px_rgba(139,92,246,0.3)]'
                : 'bg-[#121216] border border-[#27272A] text-zinc-500'
            }`}
            title="Toggle Lumina Flow Dynamic Ambient Immersion"
          >
            <Sparkles className="w-3.5 h-3.5" />
            <span className="hidden sm:inline">Lumina Flow</span>
          </button>

          <button
            onClick={() => setShowSettingsDrawer(!showSettingsDrawer)}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6]"
            title="Reader Settings"
          >
            <Settings2 className="w-4 h-4 text-zinc-300" />
          </button>
        </div>
      </div>

      {/* MAIN READING CANVAS VIEW */}
      <main
        onClick={handlePanelClick}
        className="relative z-10 w-full max-w-2xl min-h-screen flex flex-col items-center justify-center cursor-pointer transition-transform"
        style={{
          transform: `scale(${readerConfig.zoomLevel})`,
          transformOrigin: 'center top',
        }}
      >
        {readerConfig.readerMode === ReaderMode.CONTINUOUS_WEBTOON ? (
          // CONTINUOUS WEBTOON MODE (Vertical Scrolling Tiling)
          <div ref={webtoonContainerRef} className="w-full flex flex-col items-center py-12 space-y-1">
            {pages.map((imgSrc, idx) => (
              <div
                key={idx}
                className="relative w-full shadow-2xl transition-opacity duration-300"
                onMouseEnter={() => setCurrentPage(idx)}
              >
                <img
                  src={imgSrc}
                  alt={`Page ${idx + 1}`}
                  className="w-full h-auto object-contain block select-none pointer-events-none"
                  loading="lazy"
                />
                <div className="absolute bottom-2 right-2 px-2 py-0.5 rounded bg-black/60 backdrop-blur-md text-[10px] font-mono text-zinc-400">
                  {idx + 1} / {totalPages}
                </div>
              </div>
            ))}
          </div>
        ) : readerConfig.readerMode === ReaderMode.DUAL_PAGE_SPREAD ? (
          // DUAL PAGE SPREAD MODE
          <div className="w-full min-h-screen flex items-center justify-center p-4 gap-2">
            <div className="relative flex-1 max-w-[50%] bg-black/40 rounded-lg overflow-hidden border border-[#27272A]/50">
              <img
                src={pages[currentPage]}
                alt={`Page ${currentPage + 1}`}
                className="w-full max-h-[85vh] object-contain mx-auto"
              />
              <span className="absolute bottom-2 left-2 px-2 py-0.5 rounded bg-black/70 text-[10px] font-mono text-zinc-300">
                Page {currentPage + 1}
              </span>
            </div>
            {currentPage + 1 < totalPages && (
              <div className="relative flex-1 max-w-[50%] bg-black/40 rounded-lg overflow-hidden border border-[#27272A]/50">
                <img
                  src={pages[currentPage + 1]}
                  alt={`Page ${currentPage + 2}`}
                  className="w-full max-h-[85vh] object-contain mx-auto"
                />
                <span className="absolute bottom-2 right-2 px-2 py-0.5 rounded bg-black/70 text-[10px] font-mono text-zinc-300">
                  Page {currentPage + 2}
                </span>
              </div>
            )}
          </div>
        ) : (
          // SINGLE PAGE (LTR / RTL)
          <div className="w-full min-h-screen flex items-center justify-center p-4">
            <div className="relative max-w-full max-h-[90vh]">
              <img
                src={pages[currentPage]}
                alt={`Page ${currentPage + 1}`}
                className="max-h-[88vh] max-w-full object-contain mx-auto rounded-lg shadow-2xl border border-[#27272A]/40"
              />
              <div className="absolute bottom-3 left-1/2 -translate-x-1/2 px-3 py-1 rounded-full bg-black/75 backdrop-blur-md border border-[#27272A] text-xs font-mono text-zinc-300">
                {currentPage + 1} / {totalPages}
              </div>
            </div>
          </div>
        )}
      </main>

      {/* BOTTOM CONTROLS & SCRUBBER OVERLAY */}
      <div
        className={`fixed bottom-0 left-0 right-0 z-40 bg-black/95 backdrop-blur-md border-t border-[#27272A]/70 px-4 py-3 space-y-3 transition-transform duration-300 ${
          controlsVisible ? 'translate-y-0' : 'translate-y-full'
        }`}
      >
        {/* Page Scrubber Slider */}
        <div className="max-w-xl mx-auto flex items-center gap-3">
          <button
            disabled={currentPage === 0}
            onClick={() => setCurrentPage(Math.max(0, currentPage - 1))}
            className="p-1 rounded text-zinc-400 hover:text-white disabled:opacity-30"
          >
            <ChevronLeft className="w-4 h-4" />
          </button>

          <span className="text-xs font-mono font-bold text-[#00E5FF] w-8 text-right">
            {currentPage + 1}
          </span>

          <input
            type="range"
            min="0"
            max={totalPages - 1}
            value={currentPage}
            onChange={(e) => setCurrentPage(parseInt(e.target.value))}
            className="flex-1 accent-[#8B5CF6] h-1.5 bg-[#27272A] rounded-lg cursor-pointer"
          />

          <span className="text-xs font-mono text-zinc-400 w-8">
            {totalPages}
          </span>

          <button
            disabled={currentPage === totalPages - 1}
            onClick={() => setCurrentPage(Math.min(totalPages - 1, currentPage + 1))}
            className="p-1 rounded text-zinc-400 hover:text-white disabled:opacity-30"
          >
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>

        {/* Chapter Navigation & Fast Mode Toggles */}
        <div className="max-w-xl mx-auto flex items-center justify-between gap-2 pt-1 border-t border-[#27272A]/40">
          <button
            disabled={!prevChapter}
            onClick={() => {
              if (prevChapter) {
                navigate('reader', manga.id, prevChapter.id, 0);
              }
            }}
            className="px-3 py-1.5 rounded-xl bg-[#121216] border border-[#27272A] text-xs font-bold text-zinc-300 hover:text-white disabled:opacity-30 flex items-center gap-1"
          >
            <ChevronLeft className="w-3.5 h-3.5" />
            Prev Ch.
          </button>

          {/* Quick Reader Mode Picker */}
          <div className="flex items-center gap-1 bg-[#121216] p-1 rounded-xl border border-[#27272A]">
            <button
              onClick={() => updateReaderConfig({ readerMode: ReaderMode.CONTINUOUS_WEBTOON })}
              className={`p-1.5 rounded-lg text-xs font-bold ${
                readerConfig.readerMode === ReaderMode.CONTINUOUS_WEBTOON
                  ? 'bg-[#8B5CF6] text-white'
                  : 'text-zinc-400 hover:text-white'
              }`}
              title="Continuous Webtoon (Vertical Scroll)"
            >
              <AlignVerticalSpaceAround className="w-3.5 h-3.5" />
            </button>
            <button
              onClick={() => updateReaderConfig({ readerMode: ReaderMode.SINGLE_PAGE_LTR })}
              className={`p-1.5 rounded-lg text-xs font-bold ${
                readerConfig.readerMode === ReaderMode.SINGLE_PAGE_LTR
                  ? 'bg-[#8B5CF6] text-white'
                  : 'text-zinc-400 hover:text-white'
              }`}
              title="Single Page (Left to Right)"
            >
              <BookOpen className="w-3.5 h-3.5" />
            </button>
            <button
              onClick={() => updateReaderConfig({ readerMode: ReaderMode.SINGLE_PAGE_RTL })}
              className={`p-1.5 rounded-lg text-xs font-bold ${
                readerConfig.readerMode === ReaderMode.SINGLE_PAGE_RTL
                  ? 'bg-[#8B5CF6] text-white'
                  : 'text-zinc-400 hover:text-white'
              }`}
              title="Manga Direction (Right to Left)"
            >
              <Eye className="w-3.5 h-3.5" />
            </button>
            <button
              onClick={() => updateReaderConfig({ readerMode: ReaderMode.DUAL_PAGE_SPREAD })}
              className={`p-1.5 rounded-lg text-xs font-bold ${
                readerConfig.readerMode === ReaderMode.DUAL_PAGE_SPREAD
                  ? 'bg-[#8B5CF6] text-white'
                  : 'text-zinc-400 hover:text-white'
              }`}
              title="Dual Page Spread"
            >
              <Columns className="w-3.5 h-3.5" />
            </button>
          </div>

          <button
            disabled={!nextChapter}
            onClick={() => {
              if (nextChapter) {
                navigate('reader', manga.id, nextChapter.id, 0);
              }
            }}
            className="px-3 py-1.5 rounded-xl bg-[#121216] border border-[#27272A] text-xs font-bold text-zinc-300 hover:text-white disabled:opacity-30 flex items-center gap-1"
          >
            Next Ch.
            <ChevronRight className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      {/* READER SETTINGS DRAWER / SHEET */}
      {showSettingsDrawer && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-end justify-center p-0 sm:p-4">
          <div className="w-full max-w-lg bg-[#09090B] border border-[#27272A] rounded-t-2xl sm:rounded-2xl p-5 space-y-4 shadow-2xl">
            <div className="flex items-center justify-between pb-3 border-b border-[#27272A]">
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <Sliders className="w-4 h-4 text-[#8B5CF6]" />
                Reader Engine Preferences
              </h3>
              <button
                onClick={() => setShowSettingsDrawer(false)}
                className="text-xs font-bold text-[#A78BFA] px-3 py-1 rounded-lg bg-[#8B5CF6]/20"
              >
                Done
              </button>
            </div>

            {/* AMOLED Background Tint Selector */}
            <div className="space-y-2">
              <label className="text-xs font-bold text-zinc-300">AMOLED Background Canvas Tint</label>
              <div className="grid grid-cols-4 gap-2">
                {[
                  { tint: BackgroundTint.PITCH_BLACK, label: 'Pitch Black', hex: '#000000' },
                  { tint: BackgroundTint.CHARCOAL, label: 'Charcoal', hex: '#0E0E12' },
                  { tint: BackgroundTint.SEPIA, label: 'Warm Sepia', hex: '#1C1612' },
                  { tint: BackgroundTint.DEEP_SLATE, label: 'Deep Slate', hex: '#0B0F19' },
                ].map(item => (
                  <button
                    key={item.tint}
                    onClick={() => updateReaderConfig({ backgroundTint: item.tint })}
                    className={`p-2.5 rounded-xl border flex flex-col items-center gap-1.5 transition-all ${
                      readerConfig.backgroundTint === item.tint
                        ? 'border-[#00E5FF] bg-[#18181E] shadow-[0_0_10px_rgba(0,229,255,0.2)]'
                        : 'border-[#27272A] bg-[#121216]'
                    }`}
                  >
                    <div
                      className="w-5 h-5 rounded-full border border-zinc-700 flex items-center justify-center"
                      style={{ backgroundColor: item.hex }}
                    >
                      {readerConfig.backgroundTint === item.tint && (
                        <Check className="w-3 h-3 text-[#00E5FF]" />
                      )}
                    </div>
                    <span className="text-[10px] font-bold text-zinc-300">{item.label}</span>
                  </button>
                ))}
              </div>
            </div>

            {/* Zoom Slider */}
            <div className="space-y-1.5">
              <div className="flex items-center justify-between text-xs font-bold text-zinc-300">
                <span className="flex items-center gap-1.5">
                  <ZoomIn className="w-3.5 h-3.5" />
                  Zoom Scaling
                </span>
                <span className="font-mono text-[#A78BFA]">{Math.round(readerConfig.zoomLevel * 100)}%</span>
              </div>
              <input
                type="range"
                min="0.75"
                max="1.5"
                step="0.05"
                value={readerConfig.zoomLevel}
                onChange={(e) => updateReaderConfig({ zoomLevel: parseFloat(e.target.value) })}
                className="w-full accent-[#8B5CF6] h-1.5 bg-[#27272A] rounded-lg cursor-pointer"
              />
            </div>

            {/* Brightness Slider */}
            <div className="space-y-1.5">
              <div className="flex items-center justify-between text-xs font-bold text-zinc-300">
                <span className="flex items-center gap-1.5">
                  <Sun className="w-3.5 h-3.5" />
                  Brightness Modulation
                </span>
                <span className="font-mono text-amber-400">{readerConfig.brightness}%</span>
              </div>
              <input
                type="range"
                min="30"
                max="120"
                value={readerConfig.brightness}
                onChange={(e) => updateReaderConfig({ brightness: parseInt(e.target.value) })}
                className="w-full accent-amber-500 h-1.5 bg-[#27272A] rounded-lg cursor-pointer"
              />
            </div>

            {/* Lumina Flow & Keep Screen On Toggles */}
            <div className="pt-2 space-y-2 border-t border-[#27272A]">
              <div className="flex items-center justify-between">
                <div>
                  <div className="text-xs font-bold text-white">Lumina Flow Ambient Immersion</div>
                  <div className="text-[10px] text-zinc-400">Backdrop RGB glow extracted from manga panels</div>
                </div>
                <input
                  type="checkbox"
                  checked={readerConfig.enableLuminaFlow}
                  onChange={(e) => updateReaderConfig({ enableLuminaFlow: e.target.checked })}
                  className="w-4 h-4 accent-[#8B5CF6] cursor-pointer"
                />
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <div className="text-xs font-bold text-white">Keep Screen Awake</div>
                  <div className="text-[10px] text-zinc-400">Prevent display timeout during long reading loops</div>
                </div>
                <input
                  type="checkbox"
                  checked={readerConfig.keepScreenOn}
                  onChange={(e) => updateReaderConfig({ keepScreenOn: e.target.checked })}
                  className="w-4 h-4 accent-[#8B5CF6] cursor-pointer"
                />
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
