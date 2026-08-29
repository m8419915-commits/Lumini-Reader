import React, { useState } from 'react';
import {
  ArrowLeft,
  Compass,
  Calendar,
  Layers,
  Sparkles,
  BookOpen,
  ChevronRight,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

export const LuminaJourneyMapScreen: React.FC = () => {
  const { mangas, navigate, goBack } = useLumina();
  const [viewMode, setViewMode] = useState<'year' | 'session'>('year');

  const journeyYears = [
    {
      year: '2024',
      milestone: 'Transcendent Hybrid Climax',
      items: [
        { title: 'Bleach: Thousand-Year Blood War', chaptersRead: 142, hours: 28 },
        { title: 'Solo Leveling: Ragnarok', chaptersRead: 85, hours: 14 },
      ],
    },
    {
      year: '2023',
      milestone: 'Supernatural Shonen Awakening',
      items: [
        { title: 'Chainsaw Man: Part 2', chaptersRead: 90, hours: 16 },
        { title: 'Jujutsu Kaisen: Shinjuku', chaptersRead: 110, hours: 22 },
      ],
    },
    {
      year: '2022',
      milestone: 'Classic Era Immersion',
      items: [
        { title: 'One Piece: Wano & Egghead', chaptersRead: 210, hours: 45 },
        { title: 'Berserk: Millennium Falcon', chaptersRead: 60, hours: 18 },
      ],
    },
  ];

  return (
    <div className="min-h-screen bg-black text-white pb-16">
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#00E5FF] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white flex items-center gap-2">
              Journey Map
              <span className="text-[10px] bg-[#00E5FF]/20 text-[#00E5FF] border border-[#00E5FF]/40 px-1.5 py-0.5 rounded font-mono">
                Orbit
              </span>
            </h2>
            <p className="text-[11px] text-zinc-400">Multi-year reading constellations & milestones</p>
          </div>
        </div>

        <div className="flex items-center gap-1 bg-[#121216] p-1 rounded-xl border border-[#27272A]">
          <button
            onClick={() => setViewMode('year')}
            className={`px-3 py-1 rounded-lg text-xs font-bold transition-all ${
              viewMode === 'year' ? 'bg-[#00E5FF] text-black font-extrabold' : 'text-zinc-400'
            }`}
          >
            Year Orbit
          </button>
          <button
            onClick={() => setViewMode('session')}
            className={`px-3 py-1 rounded-lg text-xs font-bold transition-all ${
              viewMode === 'session' ? 'bg-[#00E5FF] text-black font-extrabold' : 'text-zinc-400'
            }`}
          >
            Sessions
          </button>
        </div>
      </header>

      <main className="max-w-xl mx-auto px-4 pt-4 space-y-6">
        {/* Orbital Summary Card */}
        <div className="p-5 rounded-2xl bg-gradient-to-br from-[#00E5FF]/15 via-[#121216] to-[#8B5CF6]/15 border border-[#00E5FF]/30 space-y-2">
          <div className="flex items-center justify-between">
            <span className="text-[10px] font-extrabold uppercase tracking-wider text-[#00E5FF] flex items-center gap-1.5">
              <Compass className="w-3.5 h-3.5" />
              Constellation Orbit Stats
            </span>
            <span className="text-xs font-mono font-bold text-amber-400">6 Series Tracked</span>
          </div>
          <h3 className="text-base font-black text-white">697 Total Chapters Completed</h3>
          <p className="text-xs text-zinc-300">
            Across 3 multi-year epochs with average session duration of 34 minutes.
          </p>
        </div>

        {/* Timeline Orbit Tree */}
        <div className="space-y-6 relative before:absolute before:inset-0 before:left-3 before:w-0.5 before:bg-gradient-to-b before:from-[#00E5FF] before:via-[#8B5CF6] before:to-zinc-800">
          {journeyYears.map((yearGroup, idx) => (
            <div key={idx} className="relative pl-8 space-y-3">
              {/* Year marker */}
              <div className="absolute left-1 top-0 -translate-x-1/2 w-5 h-5 rounded-full bg-[#121216] border-2 border-[#00E5FF] flex items-center justify-center">
                <div className="w-1.5 h-1.5 rounded-full bg-[#00E5FF]" />
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <h4 className="text-sm font-black text-white">{yearGroup.year}</h4>
                  <p className="text-xs text-[#A78BFA] font-medium">{yearGroup.milestone}</p>
                </div>
              </div>

              <div className="space-y-2">
                {yearGroup.items.map((item, i) => (
                  <div
                    key={i}
                    onClick={() => navigate('detail', 1)}
                    className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#00E5FF]/40 rounded-xl p-3 flex items-center justify-between cursor-pointer transition-colors"
                  >
                    <div className="space-y-0.5">
                      <h5 className="text-xs font-bold text-white">{item.title}</h5>
                      <p className="text-[10px] text-zinc-400 font-mono">
                        {item.chaptersRead} Chapters • {item.hours}h logged
                      </p>
                    </div>
                    <ChevronRight className="w-4 h-4 text-zinc-500" />
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
};
