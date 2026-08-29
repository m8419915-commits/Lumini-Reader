import React, { useState, useRef } from 'react';
import {
  ArrowLeft,
  Compass,
  Zap,
  Shield,
  Sparkles,
  Info,
  Maximize2,
  Minimize2,
  RotateCcw,
  Layers,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { UniverseNode } from '../../types';

interface MangaUniverseMapScreenProps {
  mangaId?: number;
}

export const MangaUniverseMapScreen: React.FC<MangaUniverseMapScreenProps> = () => {
  const { universeNodes, universeEdges, selectedNode, setSelectedNode, goBack } = useLumina();
  const [zoom, setZoom] = useState(1);
  const [pan, setPan] = useState({ x: 0, y: 0 });
  const [isDragging, setIsDragging] = useState(false);
  const [dragStart, setDragStart] = useState({ x: 0, y: 0 });

  const activeNode = selectedNode || universeNodes[0];

  const handleMouseDown = (e: React.MouseEvent) => {
    setIsDragging(true);
    setDragStart({ x: e.clientX - pan.x, y: e.clientY - pan.y });
  };

  const handleMouseMove = (e: React.MouseEvent) => {
    if (isDragging) {
      setPan({ x: e.clientX - dragStart.x, y: e.clientY - dragStart.y });
    }
  };

  const handleMouseUp = () => {
    setIsDragging(false);
  };

  const resetView = () => {
    setZoom(1);
    setPan({ x: 0, y: 0 });
  };

  return (
    <div className="min-h-screen bg-black text-white flex flex-col justify-between overflow-hidden select-none">
      {/* Top Bar */}
      <header className="z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#00E5FF] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white flex items-center gap-2">
              Universe Map
              <span className="text-[10px] bg-[#00E5FF]/20 text-[#00E5FF] border border-[#00E5FF]/40 px-1.5 py-0.5 rounded font-mono">
                Lore Orbit
              </span>
            </h2>
            <p className="text-[11px] text-zinc-400">Bleach: Thousand-Year Blood War</p>
          </div>
        </div>

        <div className="flex items-center gap-1.5 bg-[#121216] p-1 rounded-xl border border-[#27272A]">
          <button
            onClick={() => setZoom(Math.min(zoom + 0.2, 2.0))}
            className="p-1.5 rounded-lg text-zinc-300 hover:text-white hover:bg-zinc-800"
            title="Zoom In"
          >
            <Maximize2 className="w-3.5 h-3.5" />
          </button>
          <button
            onClick={() => setZoom(Math.max(zoom - 0.2, 0.6))}
            className="p-1.5 rounded-lg text-zinc-300 hover:text-white hover:bg-zinc-800"
            title="Zoom Out"
          >
            <Minimize2 className="w-3.5 h-3.5" />
          </button>
          <button
            onClick={resetView}
            className="p-1.5 rounded-lg text-zinc-300 hover:text-white hover:bg-zinc-800"
            title="Reset Map"
          >
            <RotateCcw className="w-3.5 h-3.5" />
          </button>
        </div>
      </header>

      {/* Interactive Graph Canvas */}
      <div
        className="relative flex-1 bg-[radial-gradient(#1e1e24_1px,transparent_1px)] [background-size:24px_24px] bg-[#050508] cursor-grab active:cursor-grabbing overflow-hidden"
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
        onMouseLeave={handleMouseUp}
      >
        {/* Glow backdrop */}
        <div className="absolute inset-0 pointer-events-none bg-[radial-gradient(circle_at_center,rgba(139,92,246,0.15)_0%,transparent_70%)]" />

        <div
          className="absolute inset-0 transition-transform duration-75"
          style={{
            transform: `translate(${pan.x}px, ${pan.y}px) scale(${zoom})`,
            transformOrigin: 'center center',
          }}
        >
          {/* SVG Links / Relationship Edges */}
          <svg className="absolute inset-0 w-full h-full pointer-events-none" style={{ minWidth: 800, minHeight: 600 }}>
            {universeEdges.map((edge, idx) => {
              const fromNode = universeNodes.find(n => n.id === edge.fromNodeId);
              const toNode = universeNodes.find(n => n.id === edge.toNodeId);
              if (!fromNode || !toNode) return null;

              const midX = (fromNode.x + toNode.x) / 2;
              const midY = (fromNode.y + toNode.y) / 2;

              return (
                <g key={idx}>
                  <line
                    x1={fromNode.x}
                    y1={fromNode.y}
                    x2={toNode.x}
                    y2={toNode.y}
                    stroke={edge.color}
                    strokeWidth="1.5"
                    strokeDasharray="4 4"
                    strokeOpacity="0.6"
                  />
                  <text
                    x={midX}
                    y={midY - 6}
                    fill={edge.color}
                    fontSize="9"
                    fontWeight="bold"
                    textAnchor="middle"
                    className="select-none bg-black/80 px-1"
                  >
                    {edge.relationLabel}
                  </text>
                </g>
              );
            })}
          </svg>

          {/* Character / Lore Nodes */}
          {universeNodes.map((node) => {
            const isSelected = activeNode?.id === node.id;
            return (
              <div
                key={node.id}
                onClick={(e) => {
                  e.stopPropagation();
                  setSelectedNode(node);
                }}
                className={`absolute -translate-x-1/2 -translate-y-1/2 rounded-2xl p-3 flex flex-col items-center gap-1.5 cursor-pointer transition-all ${
                  isSelected
                    ? 'bg-[#18181E] border-2 shadow-[0_0_20px_rgba(0,229,255,0.4)] scale-110 z-20'
                    : 'bg-[#121216]/90 border border-[#27272A] hover:border-white/50 z-10'
                }`}
                style={{
                  left: `${node.x}px`,
                  top: `${node.y}px`,
                  borderColor: isSelected ? node.color : undefined,
                }}
              >
                <div
                  className="w-10 h-10 rounded-full flex items-center justify-center font-bold text-xs shadow-md border"
                  style={{
                    backgroundColor: `${node.color}25`,
                    borderColor: node.color,
                    color: node.color,
                  }}
                >
                  {node.name.slice(0, 2).toUpperCase()}
                </div>
                <div className="text-center">
                  <div className="text-xs font-bold text-white whitespace-nowrap">{node.name}</div>
                  <div className="text-[9px] text-zinc-400 font-mono whitespace-nowrap">{node.role}</div>
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* LORE INSPECTOR BOTTOM SHEET */}
      {activeNode && (
        <div className="z-30 bg-[#09090B] border-t border-[#27272A] p-4 max-w-2xl mx-auto w-full shadow-2xl space-y-3">
          <div className="flex items-start justify-between">
            <div className="flex items-center gap-3">
              <div
                className="w-12 h-12 rounded-2xl flex items-center justify-center font-black text-sm border shadow-lg"
                style={{
                  backgroundColor: `${activeNode.color}20`,
                  borderColor: activeNode.color,
                  color: activeNode.color,
                }}
              >
                {activeNode.name.slice(0, 2).toUpperCase()}
              </div>
              <div>
                <h3 className="text-base font-black text-white flex items-center gap-2">
                  {activeNode.name}
                  <span className="text-[10px] px-2 py-0.5 rounded font-mono font-bold bg-[#18181E] border border-[#27272A] text-[#A78BFA]">
                    {activeNode.faction}
                  </span>
                </h3>
                <p className="text-xs font-semibold text-zinc-400">{activeNode.role}</p>
              </div>
            </div>

            <div className="text-right">
              <span className="text-[10px] text-zinc-500 font-bold uppercase tracking-wider block">
                Power Tier
              </span>
              <span className="text-xs font-bold text-[#00E5FF] font-mono">
                {activeNode.powerLevel}
              </span>
            </div>
          </div>

          <p className="text-xs text-zinc-300 bg-[#121216] border border-[#27272A] p-3 rounded-xl leading-relaxed">
            {activeNode.bio}
          </p>
        </div>
      )}
    </div>
  );
};
