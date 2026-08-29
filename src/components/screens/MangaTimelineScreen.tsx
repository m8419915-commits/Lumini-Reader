import React, { useState } from 'react';
import {
  ArrowLeft,
  GitBranch,
  BookOpen,
  Calendar,
  Layers,
  Sparkles,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

interface MangaTimelineScreenProps {
  mangaId?: number;
}

export const MangaTimelineScreen: React.FC<MangaTimelineScreenProps> = () => {
  const { goBack, navigate } = useLumina();
  const [timelineType, setTimelineType] = useState<'chronological' | 'publication'>('chronological');

  const chronologicalArcs = [
    {
      era: 'Origin & Soul Society Entry',
      chapters: 'Ch. 1 - 70',
      description: 'Ichigo awakens substitute Shinigami powers via Rukia and infiltrates Seireitei.',
      importance: 'Canon Foundation',
    },
    {
      era: 'Arrancar Incursion & Hueco Mundo',
      chapters: 'Ch. 179 - 315',
      description: 'Aizen defects with Hogyoku; Las Noches invasion to rescue Orihime.',
      importance: 'Ascension Arc',
    },
    {
      era: 'Fake Karakura Town & Deicide',
      chapters: 'Ch. 316 - 423',
      description: 'Final showdown against Aizen; Ichigo unleashes Final Getsuga Tensho.',
      importance: 'Climax Stage 1',
    },
    {
      era: 'Thousand-Year Blood War (Current)',
      chapters: 'Ch. 480 - 686',
      description: 'Quincy empire Wandenreich returns; Yhwach attacks Soul King Palace.',
      importance: 'Ultimate War',
    },
  ];

  return (
    <div className="min-h-screen bg-black text-white pb-16">
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-amber-500 transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white flex items-center gap-2">
              Timeline Guide
              <span className="text-[10px] bg-amber-500/20 text-amber-400 border border-amber-500/40 px-1.5 py-0.5 rounded font-mono">
                Bleach
              </span>
            </h2>
            <p className="text-[11px] text-zinc-400">Story arcs, canon order & chronological breakdown</p>
          </div>
        </div>

        <div className="flex items-center gap-1 bg-[#121216] p-1 rounded-xl border border-[#27272A]">
          <button
            onClick={() => setTimelineType('chronological')}
            className={`px-2.5 py-1 rounded-lg text-xs font-bold transition-all ${
              timelineType === 'chronological' ? 'bg-amber-500 text-black font-extrabold' : 'text-zinc-400'
            }`}
          >
            Lore Order
          </button>
          <button
            onClick={() => setTimelineType('publication')}
            className={`px-2.5 py-1 rounded-lg text-xs font-bold transition-all ${
              timelineType === 'publication' ? 'bg-amber-500 text-black font-extrabold' : 'text-zinc-400'
            }`}
          >
            Release
          </button>
        </div>
      </header>

      <main className="max-w-xl mx-auto px-4 pt-4 space-y-4">
        {chronologicalArcs.map((arc, idx) => (
          <div
            key={idx}
            className="bg-[#121216] border border-[#27272A] hover:border-amber-500/50 rounded-2xl p-4 space-y-2 transition-colors"
          >
            <div className="flex items-center justify-between">
              <span className="px-2 py-0.5 rounded bg-amber-500/15 border border-amber-500/30 text-amber-400 text-[10px] font-bold font-mono">
                {arc.chapters}
              </span>
              <span className="text-[10px] text-zinc-400 font-semibold">{arc.importance}</span>
            </div>

            <h3 className="text-sm font-bold text-white">{arc.era}</h3>
            <p className="text-xs text-zinc-300 leading-relaxed">{arc.description}</p>
          </div>
        ))}
      </main>
    </div>
  );
};
